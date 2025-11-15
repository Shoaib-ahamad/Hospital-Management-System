package com.hospital.ui;

import com.hospital.model.Appointment;
import com.hospital.model.AppointmentRequest;
import com.hospital.model.Patient;
import com.hospital.service.AppointmentService;
import com.hospital.service.PatientService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class PatientUI {
    private Scanner scanner;
    private PatientService patientService;
    private AppointmentService appointmentService;
    private Patient currentPatient;

    public PatientUI() {
        this.scanner = new Scanner(System.in);
        this.patientService = new PatientService();
        this.appointmentService = new AppointmentService();
    }

    public void start() {
        while (true) {
            if (currentPatient == null) {
                showLoginMenu();
            } else {
                showMainMenu();
            }
        }
    }

    private void showLoginMenu() {
        System.out.println("\n========== HOSPITAL MANAGEMENT SYSTEM - PATIENT PORTAL ==========");
        System.out.println("1. Register as New Patient");
        System.out.println("2. Login (Enter Email)");
        System.out.println("3. Exit");
        System.out.print("Choose an option: ");
        
        int choice = scanner.nextInt();
        scanner.nextLine();
        
        switch (choice) {
            case 1:
                registerPatient();
                break;
            case 2:
                loginPatient();
                break;
            case 3:
                System.out.println("Thank you for using Hospital Management System!");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid option. Please try again.");
        }
    }

    private void registerPatient() {
        System.out.println("\n========== PATIENT REGISTRATION ==========");
        System.out.print("Enter Name: ");
        String name = scanner.nextLine();
        
        System.out.print("Enter Email: ");
        String email = scanner.nextLine();
        
        System.out.print("Enter Phone: ");
        String phone = scanner.nextLine();
        
        System.out.print("Enter Age: ");
        int age = scanner.nextInt();
        scanner.nextLine();
        
        System.out.print("Enter Gender (M/F): ");
        String gender = scanner.nextLine();
        
        System.out.print("Enter Address: ");
        String address = scanner.nextLine();
        
        try {
            Patient patient = new Patient(name, email, phone, age, gender, address);
            int patientId = patientService.registerPatient(patient);
            if (patientId > 0) {
                System.out.println("\nRegistration successful! Your Patient ID is: " + patientId);
                currentPatient = patientService.getPatientById(patientId);
            } else {
                System.out.println("Registration failed. Please try again.");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void loginPatient() {
        System.out.println("\n========== PATIENT LOGIN ==========");
        System.out.print("Enter your Email: ");
        String email = scanner.nextLine();
        
        currentPatient = patientService.getPatientByEmail(email);
        if (currentPatient == null) {
            System.out.println("Patient not found with this email. Please register first.");
        } else {
            System.out.println("Welcome, " + currentPatient.getName() + "!");
        }
    }

    private void showMainMenu() {
        System.out.println("\n========== PATIENT MENU ==========");
        System.out.println("1. Request Doctor Appointment");
        System.out.println("2. View My Appointment Requests");
        System.out.println("3. View My Confirmed Appointments");
        System.out.println("4. Logout");
        System.out.print("Choose an option: ");
        
        int choice = scanner.nextInt();
        scanner.nextLine();
        
        switch (choice) {
            case 1:
                requestAppointment();
                break;
            case 2:
                viewMyRequests();
                break;
            case 3:
                viewMyAppointments();
                break;
            case 4:
                currentPatient = null;
                System.out.println("Logged out successfully.");
                break;
            default:
                System.out.println("Invalid option. Please try again.");
        }
    }

    private void requestAppointment() {
        System.out.println("\n========== REQUEST APPOINTMENT ==========");
        System.out.print("Enter Specialization (e.g., Cardiology, Neurology, General): ");
        String specialization = scanner.nextLine();
        
        System.out.print("Enter Appointment Date (yyyy-MM-dd): ");
        String dateStr = scanner.nextLine();
        
        System.out.print("Enter Description (optional): ");
        String description = scanner.nextLine();
        
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date requestedDate = sdf.parse(dateStr);
            
            int requestId = appointmentService.requestAppointment(
                currentPatient.getPatientId(),
                specialization,
                requestedDate,
                description
            );
            
            if (requestId > 0) {
                System.out.println("\nAppointment request submitted successfully!");
                System.out.println("Request ID: " + requestId);
                System.out.println("Status: PENDING - Waiting for admin approval");
            } else {
                System.out.println("Failed to submit appointment request.");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void viewMyRequests() {
        System.out.println("\n========== MY APPOINTMENT REQUESTS ==========");
        List<AppointmentRequest> requests = appointmentService.getPatientRequests(currentPatient.getPatientId());
        
        if (requests.isEmpty()) {
            System.out.println("No appointment requests found.");
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            System.out.printf("%-10s %-20s %-15s %-15s %-10s\n", 
                "Request ID", "Specialization", "Requested Date", "Status", "Description");
            System.out.println("------------------------------------------------------------------------");
            for (AppointmentRequest request : requests) {
                System.out.printf("%-10d %-20s %-15s %-15s %-10s\n",
                    request.getRequestId(),
                    request.getSpecialization(),
                    sdf.format(request.getRequestedDate()),
                    request.getStatus(),
                    request.getDescription() != null ? request.getDescription() : "N/A");
            }
        }
    }

    private void viewMyAppointments() {
        System.out.println("\n========== MY CONFIRMED APPOINTMENTS ==========");
        List<Appointment> appointments = appointmentService.getPatientAppointments(currentPatient.getPatientId());
        
        if (appointments.isEmpty()) {
            System.out.println("No confirmed appointments found.");
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            System.out.printf("%-15s %-20s %-20s %-15s %-15s\n",
                "Appointment ID", "Doctor Name", "Specialization", "Date & Time", "Status");
            System.out.println("----------------------------------------------------------------------------------------");
            for (Appointment appointment : appointments) {
                System.out.printf("%-15d %-20s %-20s %-15s %-15s\n",
                    appointment.getAppointmentId(),
                    appointment.getDoctorName(),
                    appointment.getSpecialization(),
                    sdf.format(appointment.getAppointmentDate()),
                    appointment.getStatus());
            }
        }
    }
}

