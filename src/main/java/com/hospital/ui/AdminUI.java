package com.hospital.ui;

import com.hospital.model.Appointment;
import com.hospital.model.AppointmentRequest;
import com.hospital.model.Doctor;
import com.hospital.service.AppointmentService;
import com.hospital.service.DoctorService;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Scanner;

public class AdminUI {
    private Scanner scanner;
    private AppointmentService appointmentService;
    private DoctorService doctorService;
    private static final String ADMIN_PASSWORD = "admin123"; // Simple password for demo

    public AdminUI() {
        this.scanner = new Scanner(System.in);
        this.appointmentService = new AppointmentService();
        this.doctorService = new DoctorService();
    }

    public void start() {
        if (authenticate()) {
            showMainMenu();
        } else {
            System.out.println("Authentication failed. Access denied.");
        }
    }

    private boolean authenticate() {
        System.out.println("\n========== ADMIN LOGIN ==========");
        System.out.print("Enter Admin Password: ");
        String password = scanner.nextLine();
        return ADMIN_PASSWORD.equals(password);
    }

    private void showMainMenu() {
        while (true) {
            System.out.println("\n========== ADMIN MENU ==========");
            System.out.println("1. View Pending Appointment Requests");
            System.out.println("2. View Available Doctors");
            System.out.println("3. Fix Appointment (Assign Doctor to Request)");
            System.out.println("4. View All Appointments");
            System.out.println("5. Add New Doctor");
            System.out.println("6. View All Doctors");
            System.out.println("7. Exit");
            System.out.print("Choose an option: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            switch (choice) {
                case 1:
                    viewPendingRequests();
                    break;
                case 2:
                    viewAvailableDoctors();
                    break;
                case 3:
                    fixAppointment();
                    break;
                case 4:
                    viewAllAppointments();
                    break;
                case 5:
                    addDoctor();
                    break;
                case 6:
                    viewAllDoctors();
                    break;
                case 7:
                    System.out.println("Exiting Admin Panel...");
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void viewPendingRequests() {
        System.out.println("\n========== PENDING APPOINTMENT REQUESTS ==========");
        List<AppointmentRequest> requests = appointmentService.getPendingRequests();
        
        if (requests.isEmpty()) {
            System.out.println("No pending appointment requests.");
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            System.out.printf("%-10s %-20s %-20s %-15s %-20s %-30s\n",
                "Request ID", "Patient Name", "Specialization", "Requested Date", "Status", "Description");
            System.out.println("--------------------------------------------------------------------------------------------------------");
            for (AppointmentRequest request : requests) {
                System.out.printf("%-10d %-20s %-20s %-15s %-20s %-30s\n",
                    request.getRequestId(),
                    request.getPatientName(),
                    request.getSpecialization(),
                    sdf.format(request.getRequestedDate()),
                    request.getStatus(),
                    request.getDescription() != null ? request.getDescription() : "N/A");
            }
        }
    }

    private void viewAvailableDoctors() {
        System.out.println("\n========== AVAILABLE DOCTORS ==========");
        System.out.print("Enter Specialization (or press Enter for all): ");
        String specialization = scanner.nextLine();
        
        List<Doctor> doctors;
        if (specialization.trim().isEmpty()) {
            doctors = doctorService.getAvailableDoctors();
        } else {
            doctors = doctorService.getAvailableDoctorsBySpecialization(specialization);
        }
        
        if (doctors.isEmpty()) {
            System.out.println("No available doctors found.");
        } else {
            System.out.printf("%-10s %-20s %-20s %-15s %-20s %-10s\n",
                "Doctor ID", "Name", "Specialization", "Qualification", "Experience", "Available");
            System.out.println("------------------------------------------------------------------------------------------------");
            for (Doctor doctor : doctors) {
                System.out.printf("%-10d %-20s %-20s %-15s %-20d %-10s\n",
                    doctor.getDoctorId(),
                    doctor.getName(),
                    doctor.getSpecialization(),
                    doctor.getQualification(),
                    doctor.getExperience(),
                    doctor.isAvailable() ? "Yes" : "No");
            }
        }
    }

    private void fixAppointment() {
        System.out.println("\n========== FIX APPOINTMENT ==========");
        
        // Show pending requests
        List<AppointmentRequest> requests = appointmentService.getPendingRequests();
        if (requests.isEmpty()) {
            System.out.println("No pending requests to process.");
            return;
        }
        
        System.out.println("\nPending Requests:");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (AppointmentRequest request : requests) {
            System.out.println("Request ID: " + request.getRequestId() + 
                " | Patient: " + request.getPatientName() + 
                " | Specialization: " + request.getSpecialization() + 
                " | Date: " + sdf.format(request.getRequestedDate()));
        }
        
        System.out.print("\nEnter Request ID: ");
        int requestId = scanner.nextInt();
        scanner.nextLine();
        
        // Get the request to find specialization
        AppointmentRequest selectedRequest = null;
        for (AppointmentRequest req : requests) {
            if (req.getRequestId() == requestId) {
                selectedRequest = req;
                break;
            }
        }
        
        if (selectedRequest == null) {
            System.out.println("Invalid Request ID.");
            return;
        }
        
        // Show suitable doctors
        System.out.println("\nAvailable Doctors for " + selectedRequest.getSpecialization() + ":");
        List<Doctor> suitableDoctors = appointmentService.getSuitableDoctors(selectedRequest.getSpecialization());
        
        if (suitableDoctors.isEmpty()) {
            System.out.println("No available doctors found for this specialization.");
            return;
        }
        
        for (Doctor doctor : suitableDoctors) {
            System.out.println("Doctor ID: " + doctor.getDoctorId() + 
                " | Name: " + doctor.getName() + 
                " | Qualification: " + doctor.getQualification() + 
                " | Experience: " + doctor.getExperience() + " years");
        }
        
        System.out.print("\nEnter Doctor ID to assign: ");
        int doctorId = scanner.nextInt();
        scanner.nextLine();
        
        try {
            int appointmentId = appointmentService.fixAppointment(requestId, doctorId);
            if (appointmentId > 0) {
                System.out.println("\nAppointment fixed successfully!");
                System.out.println("Appointment ID: " + appointmentId);
                System.out.println("Status: CONFIRMED");
            } else {
                System.out.println("Failed to fix appointment.");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void viewAllAppointments() {
        System.out.println("\n========== ALL APPOINTMENTS ==========");
        List<Appointment> appointments = appointmentService.getAllAppointments();
        
        if (appointments.isEmpty()) {
            System.out.println("No appointments found.");
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            System.out.printf("%-15s %-20s %-20s %-20s %-15s %-15s\n",
                "Appointment ID", "Patient Name", "Doctor Name", "Specialization", "Date & Time", "Status");
            System.out.println("--------------------------------------------------------------------------------------------------------");
            for (Appointment appointment : appointments) {
                System.out.printf("%-15d %-20s %-20s %-20s %-15s %-15s\n",
                    appointment.getAppointmentId(),
                    appointment.getPatientName(),
                    appointment.getDoctorName(),
                    appointment.getSpecialization(),
                    sdf.format(appointment.getAppointmentDate()),
                    appointment.getStatus());
            }
        }
    }

    private void addDoctor() {
        System.out.println("\n========== ADD NEW DOCTOR ==========");
        System.out.print("Enter Doctor Name: ");
        String name = scanner.nextLine();
        
        System.out.print("Enter Specialization: ");
        String specialization = scanner.nextLine();
        
        System.out.print("Enter Email: ");
        String email = scanner.nextLine();
        
        System.out.print("Enter Phone: ");
        String phone = scanner.nextLine();
        
        System.out.print("Enter Qualification: ");
        String qualification = scanner.nextLine();
        
        System.out.print("Enter Experience (years): ");
        int experience = scanner.nextInt();
        scanner.nextLine();
        
        try {
            Doctor doctor = new Doctor(name, specialization, email, phone, qualification, experience, true);
            int doctorId = doctorService.addDoctor(doctor);
            if (doctorId > 0) {
                System.out.println("\nDoctor added successfully! Doctor ID: " + doctorId);
            } else {
                System.out.println("Failed to add doctor.");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void viewAllDoctors() {
        System.out.println("\n========== ALL DOCTORS ==========");
        List<Doctor> doctors = doctorService.getAllDoctors();
        
        if (doctors.isEmpty()) {
            System.out.println("No doctors found.");
        } else {
            System.out.printf("%-10s %-20s %-20s %-15s %-20s %-10s\n",
                "Doctor ID", "Name", "Specialization", "Qualification", "Experience", "Available");
            System.out.println("------------------------------------------------------------------------------------------------");
            for (Doctor doctor : doctors) {
                System.out.printf("%-10d %-20s %-20s %-15s %-20d %-10s\n",
                    doctor.getDoctorId(),
                    doctor.getName(),
                    doctor.getSpecialization(),
                    doctor.getQualification(),
                    doctor.getExperience(),
                    doctor.isAvailable() ? "Yes" : "No");
            }
        }
    }
}

