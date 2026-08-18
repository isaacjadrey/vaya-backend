CREATE TABLE users
(
    id                      VARCHAR(36) PRIMARY KEY,
    tenant_id               VARCHAR(36),
    name                    VARCHAR(255) NOT NULL,
    email                   VARCHAR(255) NOT NULL UNIQUE,
    phone_number            VARCHAR(32),
    password_hash           VARCHAR(255) NOT NULL,
    role                    VARCHAR(32)  NOT NULL DEFAULT 'NONE',
    status                  VARCHAR(32)  NOT NULL DEFAULT 'INACTIVE',
    verification_code       VARCHAR(6),
    verification_expires_at TIMESTAMP,
    created_at              TIMESTAMP    NOT NULL DEFAULT now()
);
CREATE INDEX idx_users_tenant_id ON users (tenant_id);

CREATE TABLE user_permissions
(
    user_id    VARCHAR(36) NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    permission VARCHAR(64) NOT NULL,
    PRIMARY KEY (user_id, permission)
);

CREATE TABLE refresh_tokens
(
    id         VARCHAR(36) PRIMARY KEY,
    user_id    VARCHAR(36)  NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    token_hash VARCHAR(255) NOT NULL UNIQUE,
    expires_at TIMESTAMP    NOT NULL,
    revoked    BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP    NOT NULL DEFAULT now()
);
CREATE INDEX idx_refresh_tokens_user_id ON refresh_tokens (user_id);