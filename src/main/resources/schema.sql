-- Schema for transaction_state_lookup
CREATE TABLE IF NOT EXISTS transaction_state_lookup (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  state_name VARCHAR(255) NOT NULL UNIQUE,
  description VARCHAR(1000)
);

-- Schema for transaction_log
CREATE TABLE IF NOT EXISTS transaction_log (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  transaction_id VARCHAR(255) NOT NULL,
  state VARCHAR(255) NOT NULL,
  service_name VARCHAR(255),
  username VARCHAR(255),
  event_time TIMESTAMP NOT NULL
);

-- Indexes to optimize queries by transaction_id, state and service_name
CREATE INDEX IF NOT EXISTS idx_transaction_id ON transaction_log(transaction_id);
CREATE INDEX IF NOT EXISTS idx_state ON transaction_log(state);
CREATE INDEX IF NOT EXISTS idx_service_name ON transaction_log(service_name);

