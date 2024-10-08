--liquibase formatted sql

CREATE TABLE IF NOT EXISTS application
(
    application_id SERIAL NOT NULL,
    client_id BIGINT,
    credit_id BIGINT,
    status VARCHAR,
    creation_date TIMESTAMP,
    applied_offer JSONB,
    loan_offers JSONB,
    sign_date TIMESTAMP,
    ses_code VARCHAR,
    status_history JSONB,

    PRIMARY KEY (application_id),
    FOREIGN KEY (client_id) REFERENCES client (client_id),
    FOREIGN KEY (credit_id) REFERENCES credit (credit_id)
);