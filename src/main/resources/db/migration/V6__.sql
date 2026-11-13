CREATE TABLE reservations
(
    id           UUID                        NOT NULL,
    reference_id UUID                        NOT NULL,
    status       VARCHAR(255)                NOT NULL,
    created_at   TIMESTAMP(6) WITH TIME ZONE NOT NULL,
    CONSTRAINT pk_reservations PRIMARY KEY (id)
);

CREATE INDEX idx_reservations_reference ON reservations (reference_id);

CREATE TABLE reservation_units
(
    reservation_id UUID NOT NULL,
    unit_id        UUID NOT NULL,
    CONSTRAINT pk_reservation_units PRIMARY KEY (reservation_id, unit_id)
);

ALTER TABLE reservation_units
    ADD CONSTRAINT FK_RESERVATION_UNITS_ON_RESERVATION FOREIGN KEY (reservation_id) REFERENCES reservations (id);
ALTER TABLE reservation_units
    ADD CONSTRAINT FK_RESERVATION_UNITS_ON_UNIT FOREIGN KEY (unit_id) REFERENCES units (id);

CREATE INDEX idx_reservation_units_unit ON reservation_units (unit_id);
