ALTER TABLE outbox_events
    ADD COLUMN attempts        INTEGER NOT NULL DEFAULT 0,
    ADD COLUMN next_attempt_at TIMESTAMP(6) WITH TIME ZONE;

CREATE INDEX idx_outbox_events_pending ON outbox_events (ack, next_attempt_at, created_at);
