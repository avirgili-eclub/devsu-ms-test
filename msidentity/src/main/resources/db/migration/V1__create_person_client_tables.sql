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
