--liquibase formatted sql

CREATE TABLE IF NOT EXISTS client
(
    client_id SERIAL NOT NULL,
    last_name VARCHAR,
    first_name VARCHAR,
    middle_name VARCHAR,
    birth_date DATE,
    email VARCHAR,
    gender VARCHAR,
    marital_status VARCHAR,
    dependent_amount INT,
    passport JSONB,
    employment JSONB,
    account VARCHAR,

    PRIMARY KEY (client_id)
);