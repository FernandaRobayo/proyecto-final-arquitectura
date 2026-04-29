-- =========================================
-- VETERINARY DOMAIN - FULL SCHEMA (8 ENTITIES)
-- =========================================

-- ======================
-- TABLE: species
-- ======================
CREATE TABLE species (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(80) NOT NULL UNIQUE
);

-- ======================
-- TABLE: breed
-- ======================
CREATE TABLE breed (
    id BIGSERIAL PRIMARY KEY,
    species_id BIGINT NOT NULL,
    name VARCHAR(80) NOT NULL,
    CONSTRAINT fk_breed_species FOREIGN KEY (species_id) REFERENCES species (id)
);

-- ======================
-- TABLE: customer
-- ======================
CREATE TABLE customer (
    id BIGSERIAL PRIMARY KEY,
    full_name VARCHAR(120) NOT NULL,
    document_number VARCHAR(30) NOT NULL UNIQUE,
    phone VARCHAR(30),
    email VARCHAR(120),
    address VARCHAR(200),
    created_at TIMESTAMP DEFAULT NOW()
);

-- ======================
-- TABLE: pet
-- ======================
CREATE TABLE pet (
    id BIGSERIAL PRIMARY KEY,
    customer_id BIGINT NOT NULL,
    breed_id BIGINT NOT NULL,
    name VARCHAR(100) NOT NULL,
    gender VARCHAR(20),
    birth_date DATE,
    color VARCHAR(60),
    created_at TIMESTAMP DEFAULT NOW(),
    CONSTRAINT fk_pet_customer FOREIGN KEY (customer_id) REFERENCES customer (id),
    CONSTRAINT fk_pet_breed FOREIGN KEY (breed_id) REFERENCES breed (id)
);

-- ======================
-- TABLE: veterinarian
-- ======================
CREATE TABLE veterinarian (
    id BIGSERIAL PRIMARY KEY,
    full_name VARCHAR(120) NOT NULL,
    document_number VARCHAR(30) NOT NULL UNIQUE,
    phone VARCHAR(30),
    email VARCHAR(120),
    specialty VARCHAR(120),
    created_at TIMESTAMP DEFAULT NOW()
);

-- ======================
-- TABLE: appointment
-- ======================
CREATE TABLE appointment (
    id BIGSERIAL PRIMARY KEY,
    pet_id BIGINT NOT NULL,
    veterinarian_id BIGINT NOT NULL,
    appointment_datetime TIMESTAMP NOT NULL,
    reason VARCHAR(250) NOT NULL,
    status VARCHAR(30) NOT NULL,
    created_at TIMESTAMP DEFAULT NOW(),
    CONSTRAINT fk_appointment_pet FOREIGN KEY (pet_id) REFERENCES pet (id),
    CONSTRAINT fk_appointment_veterinarian FOREIGN KEY (veterinarian_id) REFERENCES veterinarian (id)
);

-- ======================
-- TABLE: medical_record
-- ======================
CREATE TABLE medical_record (
    id BIGSERIAL PRIMARY KEY,
    appointment_id BIGINT NOT NULL UNIQUE,
    diagnosis TEXT NOT NULL,
    notes TEXT,
    weight NUMERIC(6, 2),
    temperature NUMERIC(4, 1),
    created_at TIMESTAMP DEFAULT NOW(),
    CONSTRAINT fk_medical_record_appointment FOREIGN KEY (appointment_id) REFERENCES appointment (id)
);

-- ======================
-- TABLE: treatment
-- ======================
CREATE TABLE treatment (
    id BIGSERIAL PRIMARY KEY,
    medical_record_id BIGINT NOT NULL,
    description TEXT NOT NULL,
    medication VARCHAR(150),
    dosage VARCHAR(100),
    duration VARCHAR(100),
    created_at TIMESTAMP DEFAULT NOW(),
    CONSTRAINT fk_treatment_medical_record FOREIGN KEY (medical_record_id) REFERENCES medical_record (id)
);