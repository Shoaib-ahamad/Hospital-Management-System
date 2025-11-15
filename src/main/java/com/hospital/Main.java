package com.hospital;

import com.hospital.ui.AdminUI;
import com.hospital.ui.PatientUI;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("========================================");
        System.out.println("   HOSPITAL MANAGEMENT SYSTEM");
        System.out.println("========================================");
        System.out.println("1. Patient Portal");
        System.out.println("2. Admin Portal");
        System.out.println("3. Exit");
        System.out.print("Choose an option: ");
        
        int choice = scanner.nextInt();
        scanner.nextLine();
        
        switch (choice) {
            case 1:
                PatientUI patientUI = new PatientUI();
                patientUI.start();
                break;
            case 2:
                AdminUI adminUI = new AdminUI();
                adminUI.start();
                break;
            case 3:
                System.out.println("Thank you for using Hospital Management System!");
                System.exit(0);
                break;
            default:
                System.out.println("Invalid option.");
        }
        
        scanner.close();
    }
}

