CREATE TABLE order_items
(
    order_id   UUID NOT NULL,
    product_id UUID NOT NULL,
    quantity   INTEGER,
    price      INTEGER,
    CONSTRAINT pk_order_items PRIMARY KEY (order_id, product_id)
);

CREATE TABLE orders
(
    id          UUID NOT NULL,
    status      VARCHAR(255),
    customer_id UUID,
    total       INTEGER,
    CONSTRAINT pk_orders PRIMARY KEY (id)
);

CREATE TABLE products
(
    id    UUID NOT NULL,
    name  VARCHAR(255),
    price INTEGER,
    CONSTRAINT pk_products PRIMARY KEY (id)
);

ALTER TABLE order_items
    ADD CONSTRAINT FK_ORDER_ITEMS_ON_ORDER FOREIGN KEY (order_id) REFERENCES orders (id);
ALTER TABLE order_items
    ADD CONSTRAINT FK_ORDER_ITEMS_ON_PRODUCT FOREIGN KEY (product_id) REFERENCES products (id);