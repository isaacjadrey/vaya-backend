-- users: special case. Company-less rows (mid-registration, pre-company-creation)
-- must remain visible regardless of tenant context, or register/verify/login break.
ALTER TABLE users ENABLE ROW LEVEL SECURITY;
ALTER TABLE users FORCE ROW LEVEL SECURITY;
CREATE
POLICY tenant_isolation_users ON users
    USING (
        tenant_id IS NULL
        OR tenant_id = current_setting('app.current_tenant_id', true)
    );

ALTER TABLE company_addresses ENABLE ROW LEVEL SECURITY;
ALTER TABLE company_addresses FORCE ROW LEVEL SECURITY;
CREATE
POLICY tenant_isolation_company_addresses ON company_addresses
    USING (tenant_id = current_setting('app.current_tenant_id', true));

ALTER TABLE buses ENABLE ROW LEVEL SECURITY;
ALTER TABLE buses FORCE ROW LEVEL SECURITY;
CREATE
POLICY tenant_isolation_buses ON buses
    USING (tenant_id = current_setting('app.current_tenant_id', true));

ALTER TABLE seats ENABLE ROW LEVEL SECURITY;
ALTER TABLE seats FORCE ROW LEVEL SECURITY;
CREATE
POLICY tenant_isolation_seats ON seats
    USING (tenant_id = current_setting('app.current_tenant_id', true));

ALTER TABLE trip_schedules ENABLE ROW LEVEL SECURITY;
ALTER TABLE trip_schedules FORCE ROW LEVEL SECURITY;
CREATE
POLICY tenant_isolation_trip_schedules ON trip_schedules
    USING (tenant_id = current_setting('app.current_tenant_id', true));

ALTER TABLE trips ENABLE ROW LEVEL SECURITY;
ALTER TABLE trips FORCE ROW LEVEL SECURITY;
CREATE
POLICY tenant_isolation_trips ON trips
    USING (tenant_id = current_setting('app.current_tenant_id', true));

ALTER TABLE trip_seats ENABLE ROW LEVEL SECURITY;
ALTER TABLE trip_seats FORCE ROW LEVEL SECURITY;
CREATE
POLICY tenant_isolation_trip_seats ON trip_seats
    USING (tenant_id = current_setting('app.current_tenant_id', true));