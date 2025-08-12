-- PlacementSyncer Database Schema
-- Hybrid approach: Basic auth in Appwrite, extended data in MySQL

-- User Profiles Table (linked to Appwrite users)
CREATE TABLE user_profiles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    appwrite_user_id VARCHAR(255) UNIQUE NOT NULL,
    
    -- Personal Information
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    date_of_birth DATE,
    gender ENUM('MALE', 'FEMALE', 'OTHER'),
    profile_picture_url VARCHAR(500),
    
    -- Contact Information
    address TEXT,
    city VARCHAR(100),
    state VARCHAR(100),
    pincode VARCHAR(10),
    emergency_contact_name VARCHAR(100),
    emergency_contact_phone VARCHAR(20),
    
    -- Academic Information
    student_id VARCHAR(50) UNIQUE,
    roll_number VARCHAR(50) UNIQUE,
    department VARCHAR(100) NOT NULL,
    course VARCHAR(100) NOT NULL,
    current_year INT NOT NULL,
    current_semester INT NOT NULL,
    expected_graduation_date DATE,
    
    -- Academic Performance
    current_cgpa DECIMAL(4,2),
    current_percentage DECIMAL(5,2),
    backlogs INT DEFAULT 0,
    
    -- Documents
    resume_url VARCHAR(500),
    portfolio_url VARCHAR(500),
    linkedin_url VARCHAR(500),
    github_url VARCHAR(500),
    
    -- Profile Status
    profile_completion_percentage INT DEFAULT 0,
    is_profile_verified BOOLEAN DEFAULT FALSE,
    verification_date TIMESTAMP NULL,
    
    -- Preferences
    job_preferences TEXT, -- JSON format
    location_preferences TEXT, -- JSON format
    salary_expectations DECIMAL(12,2),
    
    -- Timestamps
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX idx_appwrite_user_id (appwrite_user_id),
    INDEX idx_student_id (student_id),
    INDEX idx_department (department),
    INDEX idx_graduation_date (expected_graduation_date)
);

-- Academic Records Table
CREATE TABLE academic_records (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_profile_id BIGINT NOT NULL,
    
    -- Education Level
    education_level ENUM('10TH', '12TH', 'DIPLOMA', 'GRADUATION', 'POST_GRADUATION') NOT NULL,
    institution_name VARCHAR(200) NOT NULL,
    board_university VARCHAR(200),
    course_name VARCHAR(100),
    specialization VARCHAR(100),
    
    -- Academic Details
    start_date DATE,
    end_date DATE,
    is_current BOOLEAN DEFAULT FALSE,
    
    -- Performance
    marks_obtained DECIMAL(8,2),
    total_marks DECIMAL(8,2),
    percentage DECIMAL(5,2),
    cgpa DECIMAL(4,2),
    grade VARCHAR(10),
    
    -- Additional Info
    achievements TEXT,
    projects TEXT, -- JSON format
    
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (user_profile_id) REFERENCES user_profiles(id) ON DELETE CASCADE,
    INDEX idx_user_education (user_profile_id, education_level)
);

-- Skills Table
CREATE TABLE skills (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_profile_id BIGINT NOT NULL,
    
    -- Skill Information
    skill_name VARCHAR(100) NOT NULL,
    skill_category ENUM('TECHNICAL', 'SOFT', 'LANGUAGE', 'CERTIFICATION') NOT NULL,
    proficiency_level ENUM('BEGINNER', 'INTERMEDIATE', 'ADVANCED', 'EXPERT') NOT NULL,
    
    -- Verification
    is_verified BOOLEAN DEFAULT FALSE,
    verification_source VARCHAR(200),
    certificate_url VARCHAR(500),
    
    -- Experience
    years_of_experience DECIMAL(3,1) DEFAULT 0.0,
    last_used_date DATE,
    
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (user_profile_id) REFERENCES user_profiles(id) ON DELETE CASCADE,
    UNIQUE KEY unique_user_skill (user_profile_id, skill_name),
    INDEX idx_skill_category (skill_category),
    INDEX idx_proficiency (proficiency_level)
);

-- Projects Table
CREATE TABLE projects (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_profile_id BIGINT NOT NULL,
    
    -- Project Information
    project_name VARCHAR(200) NOT NULL,
    project_type ENUM('ACADEMIC', 'PERSONAL', 'INTERNSHIP', 'FREELANCE', 'OPEN_SOURCE') NOT NULL,
    description TEXT,
    detailed_description TEXT,
    
    -- Timeline
    start_date DATE,
    end_date DATE,
    is_ongoing BOOLEAN DEFAULT FALSE,
    
    -- Technical Details
    technologies_used TEXT, -- JSON format
    project_role VARCHAR(100),
    team_size INT,
    
    -- Links and Files
    project_url VARCHAR(500),
    github_url VARCHAR(500),
    demo_url VARCHAR(500),
    documentation_url VARCHAR(500),
    
    -- Impact
    achievements TEXT,
    challenges_faced TEXT,
    learning_outcomes TEXT,
    
    -- Visibility
    is_featured BOOLEAN DEFAULT FALSE,
    display_order INT DEFAULT 0,
    
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (user_profile_id) REFERENCES user_profiles(id) ON DELETE CASCADE,
    INDEX idx_user_projects (user_profile_id, project_type),
    INDEX idx_featured (is_featured)
);

-- Experience Table (Internships, Jobs, etc.)
CREATE TABLE experience (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_profile_id BIGINT NOT NULL,
    
    -- Company Information
    company_name VARCHAR(200) NOT NULL,
    company_location VARCHAR(200),
    company_website VARCHAR(500),
    
    -- Position Details
    position_title VARCHAR(200) NOT NULL,
    employment_type ENUM('INTERNSHIP', 'PART_TIME', 'FULL_TIME', 'CONTRACT', 'FREELANCE') NOT NULL,
    department VARCHAR(100),
    
    -- Timeline
    start_date DATE NOT NULL,
    end_date DATE,
    is_current BOOLEAN DEFAULT FALSE,
    
    -- Work Details
    job_description TEXT,
    key_responsibilities TEXT,
    achievements TEXT,
    technologies_used TEXT, -- JSON format
    
    -- Compensation
    stipend_salary DECIMAL(12,2),
    currency VARCHAR(10) DEFAULT 'INR',
    
    -- References
    supervisor_name VARCHAR(100),
    supervisor_email VARCHAR(100),
    supervisor_phone VARCHAR(20),
    
    -- Documents
    offer_letter_url VARCHAR(500),
    experience_letter_url VARCHAR(500),
    
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (user_profile_id) REFERENCES user_profiles(id) ON DELETE CASCADE,
    INDEX idx_user_experience (user_profile_id, employment_type),
    INDEX idx_company (company_name)
);

-- Certifications Table
CREATE TABLE certifications (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_profile_id BIGINT NOT NULL,
    
    -- Certification Details
    certification_name VARCHAR(200) NOT NULL,
    issuing_organization VARCHAR(200) NOT NULL,
    certification_category VARCHAR(100),
    
    -- Dates
    issue_date DATE NOT NULL,
    expiry_date DATE,
    is_lifetime_valid BOOLEAN DEFAULT FALSE,
    
    -- Verification
    credential_id VARCHAR(200),
    credential_url VARCHAR(500),
    certificate_file_url VARCHAR(500),
    
    -- Additional Info
    description TEXT,
    skills_gained TEXT, -- JSON format
    
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (user_profile_id) REFERENCES user_profiles(id) ON DELETE CASCADE,
    INDEX idx_user_certifications (user_profile_id),
    INDEX idx_organization (issuing_organization)
);

-- Companies Table (for job applications)
CREATE TABLE companies (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    
    -- Company Information
    company_name VARCHAR(200) NOT NULL UNIQUE,
    company_description TEXT,
    industry VARCHAR(100),
    company_size ENUM('STARTUP', 'SMALL', 'MEDIUM', 'LARGE', 'ENTERPRISE'),
    
    -- Contact Information
    website VARCHAR(500),
    headquarters_location VARCHAR(200),
    hr_email VARCHAR(100),
    hr_phone VARCHAR(20),
    
    -- Recruitment Info
    campus_recruitment BOOLEAN DEFAULT TRUE,
    min_cgpa_requirement DECIMAL(4,2),
    allowed_backlogs INT DEFAULT 0,
    
    -- Company Stats
    average_package DECIMAL(12,2),
    highest_package DECIMAL(12,2),
    total_openings INT DEFAULT 0,
    
    -- Status
    is_active BOOLEAN DEFAULT TRUE,
    is_verified BOOLEAN DEFAULT FALSE,
    
    -- Metadata
    logo_url VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX idx_company_name (company_name),
    INDEX idx_industry (industry),
    INDEX idx_active (is_active)
);

-- Job Applications Table
CREATE TABLE job_applications (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_profile_id BIGINT NOT NULL,
    company_id BIGINT NOT NULL,
    
    -- Application Details
    position_title VARCHAR(200) NOT NULL,
    job_description TEXT,
    application_source ENUM('CAMPUS', 'ONLINE', 'REFERRAL', 'WALK_IN') DEFAULT 'CAMPUS',
    
    -- Application Status
    status ENUM('APPLIED', 'SCREENING', 'INTERVIEW', 'SELECTED', 'REJECTED', 'WITHDRAWN') DEFAULT 'APPLIED',
    current_round VARCHAR(100),
    
    -- Dates
    application_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    interview_date DATETIME,
    result_date DATE,
    joining_date DATE,
    
    -- Package Details
    offered_package DECIMAL(12,2),
    package_currency VARCHAR(10) DEFAULT 'INR',
    package_breakdown TEXT, -- JSON format
    
    -- Interview Details
    interview_feedback TEXT,
    interview_score DECIMAL(5,2),
    interviewer_notes TEXT,
    
    -- Documents
    application_form_url VARCHAR(500),
    resume_submitted_url VARCHAR(500),
    offer_letter_url VARCHAR(500),
    
    -- Additional Info
    notes TEXT,
    follow_up_date DATE,
    
    -- Timestamps
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    FOREIGN KEY (user_profile_id) REFERENCES user_profiles(id) ON DELETE CASCADE,
    FOREIGN KEY (company_id) REFERENCES companies(id) ON DELETE CASCADE,
    
    INDEX idx_user_applications (user_profile_id, status),
    INDEX idx_company_applications (company_id, status),
    INDEX idx_application_date (application_date)
);

-- Placement Statistics Table
CREATE TABLE placement_statistics (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_profile_id BIGINT NOT NULL,
    
    -- Overall Stats
    total_applications INT DEFAULT 0,
    applications_pending INT DEFAULT 0,
    applications_rejected INT DEFAULT 0,
    interviews_attended INT DEFAULT 0,
    offers_received INT DEFAULT 0,
    
    -- Best Metrics
    highest_package_offered DECIMAL(12,2),
    best_company_selected VARCHAR(200),
    placement_status ENUM('SEEKING', 'PLACED', 'HIGHER_STUDIES', 'ENTREPRENEUR') DEFAULT 'SEEKING',
    
    -- Profile Metrics
    profile_views INT DEFAULT 0,
    profile_score INT DEFAULT 0,
    last_activity_date TIMESTAMP,
    
    -- Calculated Fields
    success_rate DECIMAL(5,2) DEFAULT 0.00,
    average_interview_score DECIMAL(5,2),
    
    -- Updates
    last_calculated TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (user_profile_id) REFERENCES user_profiles(id) ON DELETE CASCADE,
    UNIQUE KEY unique_user_stats (user_profile_id)
);
