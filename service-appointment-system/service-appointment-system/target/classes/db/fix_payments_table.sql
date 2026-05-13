-- ============================================================
-- Run this SQL in MySQL Workbench BEFORE starting the app
-- if you have existing data with old enum values (CASH, etc.)
-- ============================================================

-- Step 1: Drop the old payments table (backup data first if needed)
DROP TABLE IF EXISTS payments;

-- Step 2: Recreate with correct VARCHAR columns (Hibernate will do this automatically)
-- The app will recreate the table on next startup with correct column types.

-- Step 3: If you want to keep old appointment data but fix payments only:
-- DELETE FROM payments WHERE method NOT IN ('CARD', 'BANK_TRANSFER');
-- ALTER TABLE payments MODIFY COLUMN method VARCHAR(30) NOT NULL;
-- ALTER TABLE payments MODIFY COLUMN status VARCHAR(30) NOT NULL;
-- ALTER TABLE payments MODIFY COLUMN invoice_number VARCHAR(50);
-- ALTER TABLE payments MODIFY COLUMN transaction_id VARCHAR(50);
-- ALTER TABLE payments MODIFY COLUMN receipt_image_path VARCHAR(255);

-- ============================================================
-- RECOMMENDED: Full clean reset (loses all test data)
-- ============================================================
-- DROP DATABASE IF EXISTS service_appointment_db;
-- CREATE DATABASE service_appointment_db;
