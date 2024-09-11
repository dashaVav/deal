--liquibase formatted sql

CREATE TABLE IF NOT EXISTS users
(
    user_id SERIAL NOT NULL,
    username VARCHAR NOT NULL UNIQUE,
    password VARCHAR NOT NULL,
    user_role VARCHAR NOT NULL,

    PRIMARY KEY (user_id)
);