CREATE TABLE routes
(
    id             UUID         NOT NULL,
    vehicle        VARCHAR(255) NOT NULL,
    start_location VARCHAR(255) NOT NULL,
    end_location   VARCHAR(255) NOT NULL,
    CONSTRAINT pk_routes PRIMARY KEY (id)
);

CREATE TABLE shipments
(
    id            UUID         NOT NULL,
    order_id      UUID         NOT NULL,
    route_id      UUID         NOT NULL,
    tracking_code VARCHAR(255) NOT NULL,
    status        VARCHAR(255) NOT NULL,
    CONSTRAINT pk_shipments PRIMARY KEY (id),
    CONSTRAINT uc_shipments_tracking_code UNIQUE (tracking_code)
);

ALTER TABLE shipments
    ADD CONSTRAINT FK_SHIPMENTS_ON_ROUTE FOREIGN KEY (route_id) REFERENCES routes (id);

CREATE INDEX idx_shipments_order ON shipments (order_id);
