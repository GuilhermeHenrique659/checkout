CREATE TABLE order_history
(
    id          UUID                        NOT NULL,
    order_id    UUID                        NOT NULL,
    status      VARCHAR(255)                NOT NULL,
    occurred_at TIMESTAMP(6) WITH TIME ZONE NOT NULL,
    next_id     UUID,
    CONSTRAINT pk_order_history PRIMARY KEY (id),
    CONSTRAINT uq_order_history_next UNIQUE (next_id)
);

ALTER TABLE order_history
    ADD CONSTRAINT FK_ORDER_HISTORY_ON_ORDER FOREIGN KEY (order_id) REFERENCES orders (id);
ALTER TABLE order_history
    ADD CONSTRAINT FK_ORDER_HISTORY_ON_NEXT FOREIGN KEY (next_id) REFERENCES order_history (id)
        DEFERRABLE INITIALLY DEFERRED;

CREATE INDEX idx_order_history_order ON order_history (order_id);

-- migrate the current status of existing orders as the head of their history
INSERT INTO order_history (id, order_id, status, occurred_at, next_id)
SELECT gen_random_uuid(), id, COALESCE(status, 'PENDING'), now(), NULL
FROM orders;

ALTER TABLE orders DROP COLUMN status;
