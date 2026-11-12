CREATE TABLE outbox_events
(
    id           UUID                        NOT NULL,
    event_type   VARCHAR(255)                NOT NULL,
    payload      TEXT                        NOT NULL,
    ack          VARCHAR(255),
    created_at   TIMESTAMP(6) WITH TIME ZONE NOT NULL,
    processed_at TIMESTAMP(6) WITH TIME ZONE,
    CONSTRAINT pk_outbox_events PRIMARY KEY (id)
);