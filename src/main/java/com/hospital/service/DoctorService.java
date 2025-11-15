package com.hospital.service;

import com.hospital.dao.DoctorDAO;
import com.hospital.model.Doctor;

import java.util.List;

public class DoctorService {
    private DoctorDAO doctorDAO;

    public DoctorService() {
        this.doctorDAO = new DoctorDAO();
    }

    public int addDoctor(Doctor doctor) {
        // Validate doctor data
        if (doctor.getName() == null || doctor.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Doctor name is required");
        }
        if (doctor.getSpecialization() == null || doctor.getSpecialization().trim().isEmpty()) {
            throw new IllegalArgumentException("Doctor specialization is required");
        }
        
        return doctorDAO.addDoctor(doctor);
    }

    public Doctor getDoctorById(int doctorId) {
        return doctorDAO.getDoctorById(doctorId);
    }

    public List<Doctor> getAllDoctors() {
        return doctorDAO.getAllDoctors();
    }

    public List<Doctor> getAvailableDoctorsBySpecialization(String specialization) {
        return doctorDAO.getAvailableDoctorsBySpecialization(specialization);
    }

    public List<Doctor> getAvailableDoctors() {
        return doctorDAO.getAvailableDoctors();
    }

    public boolean updateDoctorAvailability(int doctorId, boolean available) {
        return doctorDAO.updateDoctorAvailability(doctorId, available);
    }
}

