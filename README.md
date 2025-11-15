# Hospital Management System

A three-tier architecture Java application for managing hospital appointments between patients and doctors.

## Features

### Patient Features
- **Register/Login**: Patients can register with their details or login using email
- **Request Appointment**: Patients can request appointments with doctors by specifying:
  - Specialization needed
  - Preferred date
  - Optional description
- **View Requests**: Patients can view their appointment request status
- **View Appointments**: Patients can view their confirmed appointments

### Admin Features
- **View Pending Requests**: Admin can see all pending appointment requests from patients
- **View Available Doctors**: Admin can view all available doctors, optionally filtered by specialization
- **Fix Appointments**: Admin can assign suitable doctors to patient requests based on:
  - Specialization match
  - Doctor availability
  - Date availability
- **Manage Doctors**: Admin can add new doctors and view all doctors
- **View All Appointments**: Admin can view all confirmed appointments

## Architecture

This project follows a **Three-Tier Architecture**:

### 1. Presentation Layer (`com.hospital.ui`)
- **PatientUI**: Console-based interface for patient operations
- **AdminUI**: Console-based interface for admin operations
- **Main**: Entry point of the application

### 2. Business Logic Layer (`com.hospital.service`)
- **PatientService**: Business logic for patient operations
- **DoctorService**: Business logic for doctor operations
- **AppointmentService**: Business logic for appointment management

### 3. Data Access Layer (`com.hospital.dao`)
- **PatientDAO**: Database operations for patients
- **DoctorDAO**: Database operations for doctors
- **AppointmentDAO**: Database operations for appointments
- **AppointmentRequestDAO**: Database operations for appointment requests

### Model Layer (`com.hospital.model`)
- **Patient**: Patient entity
- **Doctor**: Doctor entity
- **Appointment**: Appointment entity
- **AppointmentRequest**: Appointment request entity

### Utility Layer (`com.hospital.util`)
- **DatabaseConnection**: Database connection management

## Prerequisites

- Java 11 or higher
- Maven 3.6 or higher
- MySQL 8.0 or higher

## Setup Instructions

### 1. Database Setup

1. Start MySQL server
2. Run the SQL script to create the database and tables:
   ```sql
   mysql -u root -p < src/main/resources/database_schema.sql
   ```
   Or manually execute the SQL file in MySQL Workbench or command line.

### 2. Database Configuration

Update the database connection details in `src/main/java/com/hospital/util/DatabaseConnection.java`:
- **URL**: `jdbc:mysql://localhost:3306/hospital_db`
- **Username**: `root` (change if different)
- **Password**: `root` (change to your MySQL password)

### 3. Build the Project

```bash
mvn clean compile
```

### 4. Run the Application

```bash
mvn exec:java -Dexec.mainClass="com.hospital.Main"
```

Or compile and run manually:
```bash
mvn clean package
java -cp target/hospital-management-system-1.0.0.jar com.hospital.Main
```

## Usage

### Patient Portal

1. Select option **1** (Patient Portal) from the main menu
2. Register as a new patient or login with email
3. Request an appointment by providing:
   - Specialization (e.g., Cardiology, Neurology, General)
   - Date (format: yyyy-MM-dd)
   - Optional description
4. View your appointment requests and confirmed appointments

### Admin Portal

1. Select option **2** (Admin Portal) from the main menu
2. Enter admin password: `admin123` (default)
3. View pending appointment requests
4. View available doctors (filtered by specialization if needed)
5. Fix appointments by:
   - Selecting a pending request
   - Choosing a suitable available doctor
   - System validates specialization match and availability
6. Manage doctors and view all appointments

## Database Schema

### Tables

- **patients**: Stores patient information
- **doctors**: Stores doctor information and availability
- **appointment_requests**: Stores patient appointment requests with status
- **appointments**: Stores confirmed appointments

### Relationships

- `appointment_requests.patient_id` → `patients.patient_id`
- `appointments.patient_id` → `patients.patient_id`
- `appointments.doctor_id` → `doctors.doctor_id`

## Project Structure

```
hospital_management/
├── pom.xml
├── README.md
└── src/
    └── main/
        ├── java/
        │   └── com/
        │       └── hospital/
        │           ├── Main.java
        │           ├── dao/          # Data Access Layer
        │           ├── model/        # Entity Classes
        │           ├── service/      # Business Logic Layer
        │           ├── ui/           # Presentation Layer
        │           └── util/         # Utility Classes
        └── resources/
            └── database_schema.sql
```

## Sample Data

The database schema includes sample data:
- 3 sample patients
- 5 sample doctors (Cardiology, Neurology, General, Pediatrics)

## Security Note

The admin password is hardcoded for demonstration purposes. In a production environment, implement proper authentication and password hashing.

## Future Enhancements

- GUI implementation (Swing/JavaFX)
- Web-based interface
- Email notifications
- Appointment cancellation
- Doctor schedule management
- Payment integration
- Medical records management

## License

This project is for educational purposes.

