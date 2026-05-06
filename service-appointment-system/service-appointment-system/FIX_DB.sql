-- ================================================================
-- SASS — Database Fix Script
-- Run this in MySQL Workbench if you see ENUM errors on startup
-- ================================================================

-- Option 1: Full clean reset (RECOMMENDED for fresh start)
-- This drops and recreates the database. All data will be lost.
DROP DATABASE IF EXISTS service_appointment_db;
CREATE DATABASE service_appointment_db
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

-- After running this, restart the Spring Boot app.
-- Hibernate will auto-create all tables with correct column types.
-- Default users and services will be seeded automatically.

-- ----------------------------------------------------------------
-- Option 2: Fix only the payments table (keeps other data)
-- ----------------------------------------------------------------
-- USE service_appointment_db;
-- 
-- ALTER TABLE payments
--   MODIFY COLUMN method  VARCHAR(30) NOT NULL,
--   MODIFY COLUMN status  VARCHAR(30) NOT NULL DEFAULT 'PENDING',
--   MODIFY COLUMN invoice_number  VARCHAR(50),
--   MODIFY COLUMN transaction_id  VARCHAR(50),
--   MODIFY COLUMN receipt_image_path VARCHAR(255),
--   MODIFY COLUMN admin_confirmed TINYINT(1) NOT NULL DEFAULT 0;
-- 
-- -- Remove rows with old enum values that no longer exist
-- DELETE FROM payments WHERE method NOT IN ('CARD','BANK_TRANSFER');
