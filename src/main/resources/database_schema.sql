-- Hospital Management System Database Schema

CREATE DATABASE IF NOT EXISTS hospital_db;
USE hospital_db;

-- Patients Table
CREATE TABLE IF NOT EXISTS patients (
    patient_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(20) NOT NULL,
    age INT NOT NULL,
    gender VARCHAR(10) NOT NULL,
    address VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Doctors Table
CREATE TABLE IF NOT EXISTS doctors (
    doctor_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    specialization VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(20) NOT NULL,
    qualification VARCHAR(100) NOT NULL,
    experience INT NOT NULL,
    available BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Appointment Requests Table
CREATE TABLE IF NOT EXISTS appointment_requests (
    request_id INT AUTO_INCREMENT PRIMARY KEY,
    patient_id INT NOT NULL,
    specialization VARCHAR(100) NOT NULL,
    requested_date DATE NOT NULL,
    status VARCHAR(20) DEFAULT 'PENDING',
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_id) REFERENCES patients(patient_id) ON DELETE CASCADE
);

-- Appointments Table
CREATE TABLE IF NOT EXISTS appointments (
    appointment_id INT AUTO_INCREMENT PRIMARY KEY,
    patient_id INT NOT NULL,
    doctor_id INT NOT NULL,
    appointment_date TIMESTAMP NOT NULL,
    status VARCHAR(20) DEFAULT 'CONFIRMED',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_id) REFERENCES patients(patient_id) ON DELETE CASCADE,
    FOREIGN KEY (doctor_id) REFERENCES doctors(doctor_id) ON DELETE CASCADE
);

-- Insert Sample Data

-- Sample Patients
INSERT INTO patients (name, email, phone, age, gender, address) VALUES
('John Doe', 'john.doe@email.com', '1234567890', 35, 'M', '123 Main St, City'),
('Jane Smith', 'jane.smith@email.com', '0987654321', 28, 'F', '456 Oak Ave, City'),
('Bob Johnson', 'bob.johnson@email.com', '1122334455', 45, 'M', '789 Pine Rd, City');

-- Sample Doctors
INSERT INTO doctors (name, specialization, email, phone, qualification, experience, available) VALUES
('Dr. Sarah Williams', 'Cardiology', 'sarah.williams@hospital.com', '1111111111', 'MD, Cardiology', 10, TRUE),
('Dr. Michael Brown', 'Neurology', 'michael.brown@hospital.com', '2222222222', 'MD, Neurology', 15, TRUE),
('Dr. Emily Davis', 'General', 'emily.davis@hospital.com', '3333333333', 'MD, General Medicine', 8, TRUE),
('Dr. James Wilson', 'Cardiology', 'james.wilson@hospital.com', '4444444444', 'MD, Cardiology', 12, TRUE),
('Dr. Lisa Anderson', 'Pediatrics', 'lisa.anderson@hospital.com', '5555555555', 'MD, Pediatrics', 7, TRUE);

