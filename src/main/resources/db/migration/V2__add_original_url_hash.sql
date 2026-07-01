ALTER TABLE urls ADD COLUMN original_url_hash VARCHAR(64) NOT NULL;

-- If data is to preserved, follow these
-- 1. Add the column, this allows nulls - initially for existing rows
-- ALTER TABLE Urls ADD COLUMN original_url_hash CHAR(64);

-- 2. Backfill existing records with the SHA-256 hash
-- UPDATE Urls SET original_url_hash = SHA2(original_url, 256) WHERE original_url_hash IS NULL;

-- 3. Enforcing this column cannot be null.
-- ALTER TABLE Urls MODIFY COLUMN original_url_hash VARCHAR(64) NOT NULL;