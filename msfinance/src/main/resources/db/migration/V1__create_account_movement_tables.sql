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
