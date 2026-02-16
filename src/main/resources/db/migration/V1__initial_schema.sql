CREATE TABLE urls (
    id BIGINT NOT NULL AUTO_INCREMENT,
    created_at DATETIME(6) DEFAULT NULL,
    expires_at DATETIME(6) DEFAULT NULL,
    original_url VARCHAR(2048) DEFAULT NULL,
    shorten_url VARCHAR(255) DEFAULT NULL,
    status ENUM('ACTIVE','DELETED','EXPIRED') DEFAULT NULL,

    PRIMARY KEY (id),
    UNIQUE KEY uk_shorten_url (shorten_url),

    INDEX idx_expires_at (expires_at),
    INDEX idx_status (statdus)

) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_0900_ai_ci;
