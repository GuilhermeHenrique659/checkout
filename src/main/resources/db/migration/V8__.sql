DROP INDEX idx_shipments_order;

ALTER TABLE shipments
    ADD CONSTRAINT uc_shipments_order UNIQUE (order_id);
