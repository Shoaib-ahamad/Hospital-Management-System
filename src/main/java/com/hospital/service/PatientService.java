package com.hospital.service;

import com.hospital.dao.PatientDAO;
import com.hospital.model.Patient;

import java.util.List;

public class PatientService {
    private PatientDAO patientDAO;

    public PatientService() {
        this.patientDAO = new PatientDAO();
    }

    public int registerPatient(Patient patient) {
        // Validate patient data
        if (patient.getName() == null || patient.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Patient name is required");
        }
        if (patient.getEmail() == null || patient.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Patient email is required");
        }
        if (patient.getPhone() == null || patient.getPhone().trim().isEmpty()) {
            throw new IllegalArgumentException("Patient phone is required");
        }
        
        // Check if patient already exists
        Patient existingPatient = patientDAO.getPatientByEmail(patient.getEmail());
        if (existingPatient != null) {
            throw new IllegalArgumentException("Patient with this email already exists");
        }
        
        return patientDAO.addPatient(patient);
    }

    public Patient getPatientById(int patientId) {
        return patientDAO.getPatientById(patientId);
    }

    public Patient getPatientByEmail(String email) {
        return patientDAO.getPatientByEmail(email);
    }

    public List<Patient> getAllPatients() {
        return patientDAO.getAllPatients();
    }
}

