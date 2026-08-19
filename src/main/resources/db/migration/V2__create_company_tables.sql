CREATE TABLE companies
(
    id              VARCHAR(36) PRIMARY KEY,
    slug            VARCHAR(255) NOT NULL UNIQUE,
    company_name    VARCHAR(255) NOT NULL,
    company_email   VARCHAR(255) NOT NULL UNIQUE,
    company_contact VARCHAR(32)  NOT NULL,
    web_url         VARCHAR(255),
    logo_url        VARCHAR(255),
    theme_config    TEXT,
    status          VARCHAR(32)  NOT NULL DEFAULT 'ACTIVE',
    created_at      TIMESTAMP    NOT NULL DEFAULT now()
);

CREATE TABLE company_operation_countries
(
    company_id   VARCHAR(36) NOT NULL REFERENCES companies (id),
    country_code VARCHAR(8)  NOT NULL,
    PRIMARY KEY (company_id, country_code)
);

CREATE TABLE company_addresses
(
    id               VARCHAR(36) PRIMARY KEY,
    company_id       VARCHAR(36)  NOT NULL REFERENCES companies (id),
    address_label    VARCHAR(100) NOT NULL,
    address_location VARCHAR(255) NOT NULL,
    created_at       TIMESTAMP    NOT NULL DEFAULT now()
);
CREATE INDEX idx_company_addresses_company_id ON company_addresses (company_id);

CREATE TABLE company_address_lines
(
    address_id VARCHAR(36)  NOT NULL REFERENCES company_addresses (id),
    line_order INT          NOT NULL,
    line       VARCHAR(255) NOT NULL,
    PRIMARY KEY (address_id, line_order)
);

-- users.tenant_id already exists from V1 as a nullable FK-shaped column;
-- add the actual constraint now that companies exists
ALTER TABLE users
    ADD CONSTRAINT fk_users_tenant FOREIGN KEY (tenant_id) REFERENCES companies (id);