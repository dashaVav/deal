--liquibase formatted sql

CREATE TABLE IF NOT EXISTS credit
(
    credit_id SERIAL NOT NULL,
    amount DECIMAL,
    term INT,
    monthly_payment DECIMAL,
    rate DECIMAL,
    psk DECIMAL,
    payment_schedule JSONB,
    is_insurance_enabled BOOLEAN,
    is_salary_client BOOLEAN,
    credit_status VARCHAR,

    PRIMARY KEY (credit_id)
);