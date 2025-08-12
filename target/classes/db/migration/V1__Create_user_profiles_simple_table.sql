-- Initial schema creation for Placement Expo
-- This script creates the user_profiles_simple table with all required fields

CREATE TABLE IF NOT EXISTS user_profiles_simple (
    id BIGSERIAL PRIMARY KEY,
    appwrite_user_id VARCHAR(255) UNIQUE NOT NULL,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    email VARCHAR(255) UNIQUE NOT NULL,
    phone VARCHAR(20),
    department VARCHAR(255),
    current_year INTEGER,
    current_cgpa DECIMAL(3,2),
    backlogs INTEGER,
    resume_url VARCHAR(500),
    linkedin_url VARCHAR(500),
    github_url VARCHAR(500),
    portfolio_url VARCHAR(500),
    profile_completion_percentage INTEGER DEFAULT 0,
    is_profile_verified BOOLEAN DEFAULT FALSE,
    ats_score INTEGER,
    ats_feedback TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes for better performance
CREATE INDEX IF NOT EXISTS idx_user_profiles_simple_appwrite_user_id ON user_profiles_simple(appwrite_user_id);
CREATE INDEX IF NOT EXISTS idx_user_profiles_simple_email ON user_profiles_simple(email);
CREATE INDEX IF NOT EXISTS idx_user_profiles_simple_department ON user_profiles_simple(department);

-- Create trigger to update updated_at timestamp
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_user_profiles_simple_updated_at 
    BEFORE UPDATE ON user_profiles_simple 
    FOR EACH ROW 
    EXECUTE FUNCTION update_updated_at_column();
