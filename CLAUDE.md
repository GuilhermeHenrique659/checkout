# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project

Spring Boot 4 / Java 25 checkout system for studying DDD and event-driven architecture (README is in Portuguese). Postgres + Flyway for persistence; Kafka is a dependency but not wired up by default.

## Commands

```bash
docker compose up -d                 # Postgres 18 on localhost:5432 (db/user/pass: checkout)
./mvnw spring-boot:run               # run the app on :8080
./mvnw test                          # all tests
./mvnw test -Dtest=ProcessPaymentTest                    # single class
./mvnw test -Dtest=ProcessPaymentTest#ShouldProcessOrderPayment  # single method
```

`@SpringBootTest` tests (`CheckoutApplicationTests`, `OrderControllerTest`) need the Postgres container running; unit tests under `*/domain` and `*/application` use in-memory fakes and don't.

Schema changes go in a new `src/main/resources/db/migration/V<n>__.sql` file — `spring.jpa.hibernate.ddl-auto=validate`, so JPA models must match the migrations exactly.

## Architecture

Code is organized by bounded context (`order`, `inventory`, `shared`) under `com.example.checkout`, each split into `api` / `application` / `domain` / `infrastructure` layers.

- **Domain is persistence-free.** Domain entities (`Order`, `OrderItem`, `Product`, `Unit`) are plain classes. JPA lives in `infrastructure/persistence` as separate `*Model` entities + Spring Data `*Dao` interfaces, and `JPA*Repository` classes implement the domain `*Repository` interfaces by mapping Model ↔ domain.
- **Use cases** are `@Service` classes in `application` with a single `execute(...)` method and nested `Input`/`Output` records in `application/dto`.
- **Errors:** `DomainError` (invariant violations) and `ApplicationError` (use-case failures), both carrying a code + message, translated to HTTP by `shared/api/GlobalExceptionHandler`.
- **Cross-context calls** go through a port owned by the consumer: `order.application.OrderInventoryService` is implemented by `inventory.InventoryService`, which adapts to inventory's own `ReserveProduct` use case. Keep `order` from depending on inventory internals directly.

### Event flow

Aggregates record domain events internally (`Order.changeStatus` appends `OrderPaid`/`OrderCanceled`/`OrderPrepared` to `order.events()`); use cases persist the aggregate and then publish those events. All events implement `shared.domain.Event` (`eventId`, `occurredAt`).

Order lifecycle, driven entirely by events:
`POST /api/orders` → `CreateOrder` publishes `OrderCreated` → `OrderCreatedSubscriber` runs `ProcessPayment` → `OrderPaid` → `OrderPaidSubscriber` runs `ReserveOrderItem` (inventory reservation).

Subscribers are `@Component`s that register handlers in their constructor via `EventBus.subscribe(EventClass, handler)`.

Messaging is abstracted behind `shared/infrastructure/queue/Publisher` and `EventBus`, with implementations chosen by properties:

| Property | Values | Default |
|---|---|---|
| `app.publisher.provider` | `spring`, `kafka`, `outbox` | `spring` |
| `app.eventBus.provider` | `spring`, `outbox` | `spring` |

`application.properties` currently sets both to `outbox`.

- `spring`: in-process via `ApplicationEventPublisher` / `@EventListener` (synchronous, same transaction).
- `outbox`: `OutboxPublisher` writes events as JSON to the `outbox_events` table (Transactional Outbox). `OutboxEventBus` is a `@Scheduled` poller: it claims `PENDING` rows one per transaction with `FOR UPDATE SKIP LOCKED`, rebuilds the event from `eventType` (simple class name of a subscribed event) + JSON payload, and runs subscribers in the same transaction. Failures retry with exponential backoff (1s, 2s, 4s…) up to max attempts; `DomainError`/`ApplicationError` fail immediately (`OutboxStatus.FAILED`). Tuned by env `OUTBOX_POLL_INTERVAL_MS`, `OUTBOX_BATCH_SIZE`, `OUTBOX_MAX_ATTEMPTS`.
- `kafka`: `KafkaPublisher` sends to a topic named after the event's simple class name; there is no Kafka consumer or Kafka service in docker-compose yet.

### Payment

`PaymentProcessor` is a gateway interface; `FakeSuccessPaymentProcessor` is the only implementation and enforces idempotency with a `Cache` (in-memory `MapCache`) keyed on `customerId:orderId:total`.

## Testing conventions

Unit tests wire use cases manually with hand-written fakes instead of mocks: `FakeOrderRepository`, `FakeProductRepository`, `FakeUnitRepository`, and `shared.FakePublisher` (captures published events for assertions). Add new fakes alongside these in `src/test` mirroring the main package.
