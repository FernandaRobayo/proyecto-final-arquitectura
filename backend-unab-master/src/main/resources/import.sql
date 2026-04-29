-- =========================================
-- VETERINARY DOMAIN - FULL SCHEMA (8 ENTITIES)
-- MySQL-compatible and idempotent
-- =========================================

CREATE TABLE IF NOT EXISTS roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(120) NOT NULL,
    username VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(120) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    enabled BIT NOT NULL DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS user_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_user_roles_user FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_user_roles_role FOREIGN KEY (role_id) REFERENCES roles (id)
);

CREATE TABLE IF NOT EXISTS species (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(80) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS breed (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    species_id BIGINT NOT NULL,
    name VARCHAR(80) NOT NULL,
    CONSTRAINT fk_breed_species FOREIGN KEY (species_id) REFERENCES species (id)
);

CREATE TABLE IF NOT EXISTS customer (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(120) NOT NULL,
    document_number VARCHAR(30) NOT NULL UNIQUE,
    phone VARCHAR(30),
    email VARCHAR(120),
    address VARCHAR(200),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS pet (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    breed_id BIGINT NOT NULL,
    name VARCHAR(100) NOT NULL,
    gender VARCHAR(20),
    birth_date DATE,
    color VARCHAR(60),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_pet_customer FOREIGN KEY (customer_id) REFERENCES customer (id),
    CONSTRAINT fk_pet_breed FOREIGN KEY (breed_id) REFERENCES breed (id)
);

CREATE TABLE IF NOT EXISTS veterinarian (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(120) NOT NULL,
    document_number VARCHAR(30) NOT NULL UNIQUE,
    phone VARCHAR(30),
    email VARCHAR(120),
    specialty VARCHAR(120),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS appointment (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    pet_id BIGINT NOT NULL,
    veterinarian_id BIGINT NOT NULL,
    appointment_datetime TIMESTAMP NOT NULL,
    reason VARCHAR(250) NOT NULL,
    status VARCHAR(30) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_appointment_pet FOREIGN KEY (pet_id) REFERENCES pet (id),
    CONSTRAINT fk_appointment_veterinarian FOREIGN KEY (veterinarian_id) REFERENCES veterinarian (id)
);

CREATE TABLE IF NOT EXISTS medical_record (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    appointment_id BIGINT NOT NULL UNIQUE,
    diagnosis TEXT NOT NULL,
    notes TEXT,
    weight DECIMAL(6, 2),
    temperature DECIMAL(4, 1),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_medical_record_appointment FOREIGN KEY (appointment_id) REFERENCES appointment (id)
);

CREATE TABLE IF NOT EXISTS treatment (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    medical_record_id BIGINT NOT NULL,
    description TEXT NOT NULL,
    medication VARCHAR(150),
    dosage VARCHAR(100),
    duration VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_treatment_medical_record FOREIGN KEY (medical_record_id) REFERENCES medical_record (id)
);
