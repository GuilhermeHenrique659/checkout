CREATE TABLE units
(
    id         UUID NOT NULL,
    product_id UUID,
    sku        VARCHAR(255),
    status     VARCHAR(255),
    CONSTRAINT pk_units PRIMARY KEY (id)
);

ALTER TABLE units
    ADD CONSTRAINT FK_UNITS_ON_PRODUCT FOREIGN KEY (product_id) REFERENCES products (id);


