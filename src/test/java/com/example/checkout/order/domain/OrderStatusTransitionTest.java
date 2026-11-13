package com.example.checkout.order.domain;

import com.example.checkout.order.domain.event.OrderCanceled;
import com.example.checkout.order.domain.event.OrderPaid;
import com.example.checkout.order.domain.event.OrderPrepared;
import com.example.checkout.shared.domain.DomainError;
import com.example.checkout.shared.domain.Event;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

import static com.example.checkout.order.domain.OrderStatus.*;

class OrderStatusTransitionTest {

    private static final Map<OrderStatus, Set<OrderStatus>> ALLOWED = Map.of(
            PENDING, Set.of(PAID, CANCEL),
            PAID, Set.of(PREPARED, CANCEL),
            PREPARED, Set.of(SHIPPED, CANCEL),
            SHIPPED, Set.of(DELIVER, CANCEL),
            DELIVER, Set.of(CANCEL),
            CANCEL, Set.of()
    );

    // shortest valid path from PENDING to each status
    private static final Map<OrderStatus, List<OrderStatus>> PATH = Map.of(
            PENDING, List.of(),
            PAID, List.of(PAID),
            PREPARED, List.of(PAID, PREPARED),
            SHIPPED, List.of(PAID, PREPARED, SHIPPED),
            DELIVER, List.of(PAID, PREPARED, SHIPPED, DELIVER),
            CANCEL, List.of(CANCEL)
    );

    private static Order orderAt(OrderStatus status) {
        var order = Order.create(UUID.randomUUID(), List.of(new OrderItem(UUID.randomUUID(), 1, 100)));
        PATH.get(status).forEach(order::changeStatus);
        return order;
    }

    static Stream<Arguments> validTransitions() {
        return Stream.of(
                Arguments.of(PENDING, PAID, OrderPaid.class),
                Arguments.of(PENDING, CANCEL, OrderCanceled.class),
                Arguments.of(PAID, PREPARED, OrderPrepared.class),
                Arguments.of(PAID, CANCEL, OrderCanceled.class),
                Arguments.of(PREPARED, SHIPPED, null),
                Arguments.of(PREPARED, CANCEL, OrderCanceled.class),
                Arguments.of(SHIPPED, DELIVER, null),
                Arguments.of(SHIPPED, CANCEL, OrderCanceled.class),
                Arguments.of(DELIVER, CANCEL, OrderCanceled.class)
        );
    }

    static Stream<Arguments> invalidTransitions() {
        return Arrays.stream(OrderStatus.values()).flatMap(from ->
                Arrays.stream(OrderStatus.values())
                        .filter(to -> !ALLOWED.get(from).contains(to))
                        .map(to -> Arguments.of(from, to)));
    }

    @ParameterizedTest(name = "{0} -> {1}")
    @MethodSource("validTransitions")
    void shouldTransitionAndAppendHistory(OrderStatus from, OrderStatus to, Class<? extends Event> expectedEvent) {
        var order = orderAt(from);
        var historyBefore = order.histories();
        var eventsBefore = order.events().size();

        order.changeStatus(to);

        Assertions.assertEquals(to, order.status());

        var history = order.histories();
        Assertions.assertEquals(historyBefore.size() + 1, history.size());
        Assertions.assertEquals(historyBefore, history.subList(0, historyBefore.size()));
        Assertions.assertEquals(history.getLast().id(), history.get(history.size() - 2).next().orElseThrow());
        Assertions.assertTrue(history.getLast().isCurrent());

        var newEvents = order.events().subList(eventsBefore, order.events().size());
        if (expectedEvent == null) {
            Assertions.assertTrue(newEvents.isEmpty());
        } else {
            Assertions.assertEquals(1, newEvents.size());
            Assertions.assertInstanceOf(expectedEvent, newEvents.getFirst());
        }
    }

    @ParameterizedTest(name = "{0} -> CANCEL keeps previous status")
    @EnumSource(value = OrderStatus.class, names = "CANCEL", mode = EnumSource.Mode.EXCLUDE)
    void shouldRecordPreviousStatusWhenCanceled(OrderStatus from) {
        var order = orderAt(from);

        order.changeStatus(CANCEL);

        var event = Assertions.assertInstanceOf(OrderCanceled.class, order.events().getLast());
        Assertions.assertEquals(order.id(), event.orderId());
        Assertions.assertEquals(from, event.previousStatus());
    }

    @ParameterizedTest(name = "{0} -> {1} is rejected")
    @MethodSource("invalidTransitions")
    void shouldRejectInvalidTransitionWithoutSideEffects(OrderStatus from, OrderStatus to) {
        var order = orderAt(from);
        var historyBefore = order.histories();
        var eventsBefore = order.events();

        var error = Assertions.assertThrowsExactly(DomainError.class, () -> order.changeStatus(to));

        Assertions.assertEquals(from == CANCEL ? "CANCELED_ORDER" : "INVALID_STATUS", error.getCode());
        Assertions.assertEquals(from, order.status());
        Assertions.assertEquals(historyBefore, order.histories());
        Assertions.assertEquals(eventsBefore, order.events());
    }
}
