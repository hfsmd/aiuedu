-- Initialization script for AIUEducation database
-- This script will be executed when the database container starts for the first time

-- Create database if not exists (PostgreSQL automatically creates the database specified in POSTGRES_DB)
-- But we can add some initial setup here if needed

-- Set timezone
SET timezone = 'UTC';

-- Create extensions if needed
-- CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- You can add initial data here if needed
-- For example, create admin user, default categories, etc.

-- Example: Create initial admin user (uncomment if needed)
-- INSERT INTO users (username, password, email, role, created_at) 
-- VALUES ('admin', '$2a$10$...', 'admin@example.com', 'ADMIN', NOW())
-- ON CONFLICT (username) DO NOTHING; 