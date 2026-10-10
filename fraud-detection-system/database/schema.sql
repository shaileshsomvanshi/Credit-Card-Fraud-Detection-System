-- Optional: Hibernate (ddl-auto=update) creates this table automatically.
CREATE DATABASE IF NOT EXISTS fraud_db;
USE fraud_db;

CREATE TABLE IF NOT EXISTS transactions (
    id                BIGINT AUTO_INCREMENT PRIMARY KEY,
    card_hash         VARCHAR(64)    NOT NULL,
    card_last4        VARCHAR(4)     NOT NULL,
    card_holder_name  VARCHAR(255)   NOT NULL,
    amount            DECIMAL(12,2)  NOT NULL,
    merchant          VARCHAR(255)   NOT NULL,
    merchant_category VARCHAR(255)   NOT NULL,
    country           VARCHAR(2)     NOT NULL,
    home_country      VARCHAR(2)     NOT NULL,
    transaction_time  DATETIME(6)    NOT NULL,
    status            VARCHAR(10)    NOT NULL,
    risk_score        INT            NOT NULL DEFAULT 0,
    fraud_reasons     VARCHAR(600),
    reviewed          BIT(1)         NOT NULL DEFAULT 0,
    INDEX idx_card_hash (card_hash),
    INDEX idx_status (status)
);
