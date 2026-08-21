CREATE TABLE buses
(
    id            VARCHAR(36) PRIMARY KEY,
    tenant_id     VARCHAR(36)  NOT NULL,
    bus_name      VARCHAR(255) NOT NULL,
    license_plate VARCHAR(32)  NOT NULL,
    bus_brand     VARCHAR(64)  NOT NULL,
    total_seats   INT          NOT NULL,
    layout_type   VARCHAR(32)  NOT NULL,
    status        VARCHAR(32)  NOT NULL DEFAULT 'ACTIVE',
    status_reason VARCHAR(255),
    created_at    TIMESTAMP    NOT NULL DEFAULT now(),
    UNIQUE (tenant_id, license_plate)
);
CREATE INDEX idx_buses_tenant_id ON buses (tenant_id);

CREATE TABLE bus_amenities
(
    bus_id  VARCHAR(36) NOT NULL REFERENCES buses (id),
    amenity VARCHAR(64) NOT NULL,
    PRIMARY KEY (bus_id, amenity)
);

CREATE TABLE seats
(
    id          VARCHAR(36) PRIMARY KEY,
    tenant_id   VARCHAR(36) NOT NULL,
    bus_id      VARCHAR(36) NOT NULL REFERENCES buses (id),
    seat_number VARCHAR(8)  NOT NULL,
    deck        INT         NOT NULL DEFAULT 1,
    row_no      INT         NOT NULL,
    column_no   INT         NOT NULL,
    seat_type   VARCHAR(32) NOT NULL,
    is_active   BOOLEAN     NOT NULL DEFAULT TRUE,
    UNIQUE (bus_id, seat_number)
);
CREATE INDEX idx_seats_bus_id ON seats (bus_id);

CREATE TABLE trip_schedules
(
    id             VARCHAR(36) PRIMARY KEY,
    tenant_id      VARCHAR(36)    NOT NULL,
    route_id       VARCHAR(36)    NOT NULL,
    bus_id         VARCHAR(36)    NOT NULL REFERENCES buses (id),
    departure_time TIME           NOT NULL,
    base_fare      NUMERIC(12, 2) NOT NULL,
    is_recurring   BOOLEAN        NOT NULL DEFAULT TRUE,
    is_active      BOOLEAN        NOT NULL DEFAULT TRUE,
    created_at     TIMESTAMP      NOT NULL DEFAULT now()
);
CREATE INDEX idx_trip_schedules_tenant_id ON trip_schedules (tenant_id);

CREATE TABLE trip_schedule_recur_days
(
    schedule_id VARCHAR(36) NOT NULL REFERENCES trip_schedules (id),
    day_of_week VARCHAR(16) NOT NULL,
    PRIMARY KEY (schedule_id, day_of_week)
);

CREATE TABLE trips
(
    id             VARCHAR(36) PRIMARY KEY,
    tenant_id      VARCHAR(36)    NOT NULL,
    schedule_id    VARCHAR(36)    NOT NULL REFERENCES trip_schedules (id),
    bus_id         VARCHAR(36)    NOT NULL REFERENCES buses (id),
    route_id       VARCHAR(36)    NOT NULL,
    trip_date      DATE           NOT NULL,
    departure_time TIME           NOT NULL,
    arrival_time   TIME           NOT NULL,
    fare           NUMERIC(12, 2) NOT NULL,
    status         VARCHAR(32)    NOT NULL DEFAULT 'SCHEDULED',
    created_at     TIMESTAMP      NOT NULL DEFAULT now(),
    UNIQUE (schedule_id, trip_date)
);
CREATE INDEX idx_trips_tenant_id ON trips (tenant_id);
CREATE INDEX idx_trips_trip_date ON trips (trip_date);

CREATE TABLE trip_seats
(
    id              VARCHAR(36) PRIMARY KEY,
    tenant_id       VARCHAR(36)    NOT NULL,
    trip_id         VARCHAR(36)    NOT NULL REFERENCES trips (id),
    seat_id         VARCHAR(36)    NOT NULL REFERENCES seats (id),
    seat_number     VARCHAR(8)     NOT NULL,
    fare            NUMERIC(12, 2) NOT NULL,
    status          VARCHAR(16)    NOT NULL DEFAULT 'AVAILABLE',
    held_by_user_id VARCHAR(36),
    hold_expires_at TIMESTAMP
);
CREATE UNIQUE INDEX uq_trip_seat_active ON trip_seats (trip_id, seat_number) WHERE status IN ('HELD','BOOKED');
CREATE INDEX idx_trip_seats_trip_id ON trip_seats (trip_id);

CREATE TABLE locations
(
    id         VARCHAR(36) PRIMARY KEY,
    tenant_id  VARCHAR(36)  NOT NULL,
    name       VARCHAR(255) NOT NULL,
    latitude   DOUBLE PRECISION,
    longitude  DOUBLE PRECISION,
    created_at TIMESTAMP    NOT NULL DEFAULT now(),
    UNIQUE (tenant_id, name)
);
CREATE INDEX idx_locations_tenant_id ON locations (tenant_id);
ALTER TABLE locations ENABLE ROW LEVEL SECURITY;
ALTER TABLE locations FORCE ROW LEVEL SECURITY;
CREATE
POLICY tenant_isolation_locations ON locations
    USING (tenant_id = current_setting('app.current_tenant_id', true));

CREATE TABLE routes
(
    id                      VARCHAR(36) PRIMARY KEY,
    tenant_id               VARCHAR(36)    NOT NULL,
    route_name              VARCHAR(255)   NOT NULL,
    origin_location_id      VARCHAR(36)    NOT NULL REFERENCES locations (id),
    destination_location_id VARCHAR(36)    NOT NULL REFERENCES locations (id),
    base_fare               NUMERIC(12, 2) NOT NULL,
    distance_km             NUMERIC(8, 2),
    est_duration_minutes    INT,
    is_active               BOOLEAN        NOT NULL DEFAULT TRUE,
    created_at              TIMESTAMP      NOT NULL DEFAULT now()

);
CREATE INDEX idx_routes_tenant_id ON routes (tenant_id);
ALTER TABLE routes ENABLE ROW LEVEL SECURITY;
ALTER TABLE routes FORCE ROW LEVEL SECURITY;
CREATE
POLICY tenant_isolation_routes ON routes
    USING (tenant_id = current_setting('app.current_tenant_id', true));

CREATE TABLE route_stops
(
    id                    VARCHAR(36) PRIMARY KEY,
    tenant_id             VARCHAR(36)    NOT NULL,
    route_id              VARCHAR(36)    NOT NULL REFERENCES routes (id),
    stop_location_id      VARCHAR(36)    NOT NULL REFERENCES locations (id),
    stop_price            NUMERIC(12, 2) NOT NULL,
    stop_distance_km      NUMERIC(8, 2),
    stop_duration_minutes INT,
    stop_order            INT            NOT NULL,
    stop_color            VARCHAR(16)    NOT NULL,
    created_at            TIMESTAMP      NOT NULL DEFAULT now(),
    UNIQUE (route_id, stop_order)
);
CREATE INDEX idx_route_stops_route_id ON route_stops (route_id);
ALTER TABLE route_stops ENABLE ROW LEVEL SECURITY;
ALTER TABLE route_stops FORCE ROW LEVEL SECURITY;
CREATE
POLICY tenant_isolation_route_stops ON route_stops
    USING (tenant_id = current_setting('app.current_tenant_id', true));