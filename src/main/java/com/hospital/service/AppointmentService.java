package com.hospital.service;

import com.hospital.dao.AppointmentDAO;
import com.hospital.dao.AppointmentRequestDAO;
import com.hospital.dao.DoctorDAO;
import com.hospital.dao.PatientDAO;
import com.hospital.model.Appointment;
import com.hospital.model.AppointmentRequest;
import com.hospital.model.Doctor;

import java.util.Date;
import java.util.List;

public class AppointmentService {
    private AppointmentDAO appointmentDAO;
    private AppointmentRequestDAO requestDAO;
    private DoctorDAO doctorDAO;
    private PatientDAO patientDAO;

    public AppointmentService() {
        this.appointmentDAO = new AppointmentDAO();
        this.requestDAO = new AppointmentRequestDAO();
        this.doctorDAO = new DoctorDAO();
        this.patientDAO = new PatientDAO();
    }

    public int requestAppointment(int patientId, String specialization, Date requestedDate, String description) {
        // Validate patient exists
        if (patientDAO.getPatientById(patientId) == null) {
            throw new IllegalArgumentException("Patient not found");
        }
        
        // Validate specialization
        if (specialization == null || specialization.trim().isEmpty()) {
            throw new IllegalArgumentException("Specialization is required");
        }
        
        // Validate date
        if (requestedDate == null) {
            throw new IllegalArgumentException("Appointment date is required");
        }
        
        // Check if date is in the past
        if (requestedDate.before(new Date())) {
            throw new IllegalArgumentException("Cannot request appointment for a past date");
        }
        
        AppointmentRequest request = new AppointmentRequest(patientId, specialization, requestedDate, "PENDING");
        request.setDescription(description);
        
        return requestDAO.addAppointmentRequest(request);
    }

    public List<AppointmentRequest> getPendingRequests() {
        return requestDAO.getAllPendingRequests();
    }

    public List<AppointmentRequest> getPatientRequests(int patientId) {
        return requestDAO.getRequestsByPatientId(patientId);
    }

    public List<Doctor> getSuitableDoctors(String specialization) {
        return doctorDAO.getAvailableDoctorsBySpecialization(specialization);
    }

    public int fixAppointment(int requestId, int doctorId) {
        // Get the request
        AppointmentRequest request = requestDAO.getRequestById(requestId);
        if (request == null) {
            throw new IllegalArgumentException("Appointment request not found");
        }
        
        if (!"PENDING".equals(request.getStatus())) {
            throw new IllegalArgumentException("Request is not pending");
        }
        
        // Get the doctor
        Doctor doctor = doctorDAO.getDoctorById(doctorId);
        if (doctor == null) {
            throw new IllegalArgumentException("Doctor not found");
        }
        
        // Check if doctor is available
        if (!doctor.isAvailable()) {
            throw new IllegalArgumentException("Doctor is not available");
        }
        
        // Check if doctor matches specialization
        if (!doctor.getSpecialization().equalsIgnoreCase(request.getSpecialization())) {
            throw new IllegalArgumentException("Doctor specialization does not match request");
        }
        
        // Check if doctor is available on the requested date
        if (!appointmentDAO.checkDoctorAvailability(doctorId, request.getRequestedDate())) {
            throw new IllegalArgumentException("Doctor is not available on the requested date");
        }
        
        // Create appointment
        Appointment appointment = new Appointment(
            request.getPatientId(),
            doctorId,
            request.getRequestedDate(),
            "CONFIRMED"
        );
        
        int appointmentId = appointmentDAO.addAppointment(appointment);
        
        if (appointmentId > 0) {
            // Update request status
            requestDAO.updateRequestStatus(requestId, "APPROVED");
        }
        
        return appointmentId;
    }

    public List<Appointment> getAllAppointments() {
        return appointmentDAO.getAllAppointments();
    }

    public List<Appointment> getPatientAppointments(int patientId) {
        return appointmentDAO.getAppointmentsByPatientId(patientId);
    }

    public Appointment getAppointmentById(int appointmentId) {
        return appointmentDAO.getAppointmentById(appointmentId);
    }
}

