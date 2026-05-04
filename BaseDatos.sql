-- ============================================================
-- BaseDatos.sql
-- Devsu Microservices Test — Full schema for both services
-- ============================================================


-- ============================================================
-- msidentity — Schema
-- ============================================================

CREATE TABLE person (
    id             BIGSERIAL    PRIMARY KEY,
    name           VARCHAR(255) NOT NULL,
    gender         VARCHAR(50),
    age            INTEGER,
    identification VARCHAR(255) NOT NULL,
    address        VARCHAR(255),
    phone          VARCHAR(50),
    CONSTRAINT uk_person_identification UNIQUE (identification)
);

CREATE TABLE client (
    id        BIGINT       NOT NULL,
    client_id VARCHAR(255) NOT NULL,
    password  VARCHAR(255) NOT NULL,
    status    BOOLEAN      NOT NULL,
    CONSTRAINT pk_client          PRIMARY KEY (id),
    CONSTRAINT fk_client_person   FOREIGN KEY (id) REFERENCES person (id),
    CONSTRAINT uk_client_clientid UNIQUE (client_id)
);


-- ============================================================
-- msfinance — Schema
-- ============================================================

CREATE TABLE account (
    id              BIGSERIAL      PRIMARY KEY,
    account_number  VARCHAR(255)   NOT NULL,
    account_type    VARCHAR(50)    NOT NULL,
    initial_balance NUMERIC(15, 2) NOT NULL,
    current_balance NUMERIC(15, 2) NOT NULL,
    status          BOOLEAN        NOT NULL,
    client_id       BIGINT         NOT NULL,
    CONSTRAINT uk_account_number UNIQUE (account_number)
);

CREATE TABLE movement (
    id            BIGSERIAL      PRIMARY KEY,
    date          TIMESTAMP      NOT NULL,
    movement_type VARCHAR(50)    NOT NULL,
    amount        NUMERIC(15, 2) NOT NULL,
    balance       NUMERIC(15, 2) NOT NULL,
    account_id    BIGINT         NOT NULL,
    CONSTRAINT fk_movement_account FOREIGN KEY (account_id) REFERENCES account (id)
);

CREATE TABLE processed_event (
    event_id     VARCHAR(36) PRIMARY KEY,
    processed_at TIMESTAMP   NOT NULL
);

CREATE TABLE known_client (
    client_id BIGINT NOT NULL,
    CONSTRAINT pk_known_client PRIMARY KEY (client_id)
);
