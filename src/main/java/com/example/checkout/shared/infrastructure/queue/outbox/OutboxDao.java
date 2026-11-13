package com.example.checkout.shared.infrastructure.queue.outbox;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface OutboxDao extends JpaRepository<OutboxModel, UUID> {

    /**
     * Locks the oldest event that is due (never tried, or its retry backoff has elapsed) with the given status whose type is in {@code eventTypes}.
     * SKIP LOCKED makes concurrent pollers (threads or app instances) skip rows already
     * held by another transaction instead of blocking or processing them twice.
     * Must be called inside a transaction; the lock is held until it commits/rolls back.
     */
    @Query(value = """
            SELECT * FROM outbox_events
            WHERE ack = :status
              AND event_type IN (:eventTypes)
              AND (next_attempt_at IS NULL OR next_attempt_at <= now())
            ORDER BY COALESCE(next_attempt_at, created_at)
            LIMIT 1
            FOR UPDATE SKIP LOCKED
            """, nativeQuery = true)
    Optional<OutboxModel> lockNext(@Param("status") String status,
                                   @Param("eventTypes") Collection<String> eventTypes);
}
