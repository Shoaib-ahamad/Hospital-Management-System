# Hospital Management System — Complete Project Documentation

## Table of Contents
1. [Project Overview](#project-overview)
2. [Technologies Used](#technologies-used)
3. [Architecture](#architecture)
4. [Layer Responsibilities](#layer-responsibilities)
5. [Database Schema](#database-schema)
6. [Core Features](#core-features)
7. [Project Structure](#project-structure)
8. [Runtime Flow](#runtime-flow)
9. [Configuration](#configuration)
10. [Build & Run Commands](#build--run-commands)
11. [Design Patterns](#design-patterns)
12. [Dependencies](#dependencies)

---

## Project Overview

A **three-tier Java application** for managing hospital appointments between patients and doctors. It demonstrates enterprise architecture patterns with console-based UI, business logic layer, and database persistence using JDBC.

**Purpose**: 
- Provide a simple command-line hospital management system demonstrating a three-tier architecture (UI → Service → DAO) using Java and MySQL
- Allow patients to register, login, request appointments, and view appointments
- Allow administrators to manage doctors and appointments
- Serve as an educational example of JDBC usage, DAO pattern, and small-scale CRUD workflows

---

## Technologies Used

| Category | Technology | Version | Purpose |
|----------|-----------|---------|---------|
| **Language** | Java | 11+ | Core application logic |
| **Build Tool** | Maven | 3.6+ | Project build and dependency management |
| **Database** | MySQL | 8.0+ | Persistent data storage |
| **JDBC Driver** | mysql-connector-j | 8.0.33 | MySQL connectivity |
| **Testing** | JUnit | 4.13.2 | Unit testing (dependency available) |

---

## Architecture

### Three-Tier Model

```
┌─────────────────────────────────────┐
│   PRESENTATION LAYER (com.hospital.ui)
│   - Main.java (entry point)
│   - PatientUI.java
│   - AdminUI.java
└────────────────┬────────────────────┘
                 ↓
┌─────────────────────────────────────┐
│   BUSINESS LOGIC LAYER (com.hospital.service)
│   - PatientService
│   - DoctorService
│   - AppointmentService
└────────────────┬────────────────────┘
                 ↓
┌─────────────────────────────────────┐
│   DATA ACCESS LAYER (com.hospital.dao)
│   - PatientDAO
│   - DoctorDAO
│   - AppointmentDAO
│   - AppointmentRequestDAO
└────────────────┬────────────────────┘
                 ↓
┌─────────────────────────────────────┐
│   DATABASE (MySQL)
│   - patients
│   - doctors
│   - appointments
│   - appointment_requests
└─────────────────────────────────────┘
```

### Data Flow

1. **User Input** → UI Layer (PatientUI / AdminUI)
2. **Business Logic** → Service Layer (PatientService / DoctorService / AppointmentService)
3. **Database Operations** → DAO Layer (PatientDAO / DoctorDAO / etc.)
4. **Results** → Back through layers to UI for display

---

## Layer Responsibilities

### 1. Presentation Layer (`com.hospital.ui`)

**Components:**
- **Main.java**: Application entry point
  - Displays main menu: "1. Patient Portal", "2. Admin Portal", "3. Exit"
  - Routes user to appropriate UI based on selection
  
- **PatientUI.java**: Patient-facing console interface
  - Registration/Login menu
  - Request appointment form
  - View requests and appointments
  
- **AdminUI.java**: Admin-facing console interface
  - View pending requests
  - View available doctors (with specialization filter)
  - Assign doctors to requests
  - Manage doctors (add, view)
  - View all appointments

**Responsibilities:**
- Display menus and options
- Collect user input via Scanner
- Call service layer methods
- Display results to users
- Handle user flow and navigation
- Format output for console display

---

### 2. Business Logic Layer (`com.hospital.service`)

**Components:**

**PatientService.java**
- Validates patient data (name, email, phone required)
- Checks for duplicate emails
- Manages patient operations
- Methods: `registerPatient()`, `getPatientById()`, `getPatientByEmail()`, `getAllPatients()`

**DoctorService.java**
- Validates doctor data (name, specialization, email, phone, qualification, experience required)
- Manages doctor operations
- Methods: `addDoctor()`, `getAllDoctors()`, `getDoctorsBySpecialization()`, `getDoctorById()`

**AppointmentService.java**
- Validates appointment requests
- Checks date validity
- Manages appointment creation
- Validates doctor availability and specialization match
- Methods: `requestAppointment()`, `fixAppointment()`, `getAppointmentsByPatientId()`, etc.

**Responsibilities:**
- Implement business rules and validation logic
- Validate input data before DAO operations
- Coordinate between multiple DAOs
- Handle business logic exceptions
- Ensure data integrity and consistency
- Prevent invalid operations (e.g., duplicate emails, scheduling conflicts)

---

### 3. Data Access Layer (`com.hospital.dao`)

**Components:**

**PatientDAO.java**
- SQL Operations:
  - `INSERT INTO patients (...)` - Add new patient
  - `SELECT * FROM patients WHERE email=?` - Get patient by email
  - `SELECT * FROM patients WHERE patient_id=?` - Get patient by ID
  - `SELECT * FROM patients` - Get all patients
  - `UPDATE patients SET ...` - Update patient

**DoctorDAO.java**
- SQL Operations:
  - `INSERT INTO doctors (...)` - Add new doctor
  - `SELECT * FROM doctors` - Get all doctors
  - `SELECT * FROM doctors WHERE doctor_id=?` - Get doctor by ID
  - `SELECT * FROM doctors WHERE specialization=?` - Get doctors by specialization
  - `UPDATE doctors SET available=?` - Update doctor availability

**AppointmentDAO.java**
- SQL Operations:
  - `INSERT INTO appointments (...)` - Create appointment
  - `SELECT * FROM appointments` - Get all appointments
  - `SELECT * FROM appointments WHERE patient_id=?` - Get patient appointments
  - `SELECT * FROM appointments WHERE appointment_id=?` - Get specific appointment
  - `UPDATE appointments SET status=?` - Update appointment status

**AppointmentRequestDAO.java**
- SQL Operations:
  - `INSERT INTO appointment_requests (...)` - Create request
  - `SELECT * FROM appointment_requests WHERE status='PENDING'` - Get pending requests
  - `SELECT * FROM appointment_requests` - Get all requests
  - `SELECT * FROM appointment_requests WHERE request_id=?` - Get specific request
  - `UPDATE appointment_requests SET status=?` - Update request status

**Responsibilities:**
- Execute SQL queries using `PreparedStatement` (prevents SQL injection)
- Manage JDBC resources (Connection, Statement, ResultSet)
- Use try-with-resources for automatic resource cleanup
- Map database rows to model objects
- Handle SQL exceptions and log errors
- Provide abstraction layer for database operations

---

### 4. Utility Layer (`com.hospital.util`)

**DatabaseConnection.java**
- **Static initialization block**: Registers MySQL JDBC driver (`com.mysql.cj.jdbc.Driver`)
- **loadProperties()**: Loads `db.properties` from classpath
- **getConnection()**: Returns new JDBC Connection for each call
  - Reads database URL, username, password from properties
  - Throws RuntimeException on connection failure
- **Resource Management**: Callers close connections (try-with-resources pattern)

**Key Features:**
- Lazy-loaded properties (loaded once, reused)
- Exception handling with descriptive messages
- Support for database configuration via external properties file

---

### 5. Model Layer (`com.hospital.model`)

**Patient.java**
```
Fields: patientId, name, email, phone, age, gender, address
Constructors: Default, with ID, without ID
Getters/Setters: All fields
```

**Doctor.java**
```
Fields: doctorId, name, specialization, email, phone, qualification, experience, available
Constructors: Default, with ID, without ID
Getters/Setters: All fields
```

**Appointment.java**
```
Fields: appointmentId, patientId, doctorId, appointmentDate, status
Getters/Setters: All fields
```

**AppointmentRequest.java**
```
Fields: requestId, patientId, specialization, requestedDate, status, description
Getters/Setters: All fields
```

---

## Database Schema

### Overview
- **Database Name**: `hospital_db`
- **Tables**: 4 (patients, doctors, appointments, appointment_requests)
- **Relationships**: Foreign keys with CASCADE DELETE
- **Engine**: InnoDB (supports foreign keys and transactions)

### Table: `patients`
```sql
CREATE TABLE patients (
    patient_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(20) NOT NULL,
    age INT NOT NULL,
    gender VARCHAR(10) NOT NULL,
    address VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```
**Indices**: PRIMARY KEY (patient_id), UNIQUE (email)
**Sample Data**: 3 patients pre-loaded

---

### Table: `doctors`
```sql
CREATE TABLE doctors (
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
```
**Indices**: PRIMARY KEY (doctor_id), UNIQUE (email)
**Specializations**: Cardiology, Neurology, General, Pediatrics
**Sample Data**: 5 doctors pre-loaded (all available)

---

### Table: `appointment_requests`
```sql
CREATE TABLE appointment_requests (
    request_id INT AUTO_INCREMENT PRIMARY KEY,
    patient_id INT NOT NULL,
    specialization VARCHAR(100) NOT NULL,
    requested_date DATE NOT NULL,
    status VARCHAR(20) DEFAULT 'PENDING',
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_id) REFERENCES patients(patient_id) ON DELETE CASCADE
);
```
**Indices**: PRIMARY KEY (request_id), FOREIGN KEY (patient_id)
**Status Values**: PENDING, APPROVED
**Relationships**: Links to patients table

---

### Table: `appointments`
```sql
CREATE TABLE appointments (
    appointment_id INT AUTO_INCREMENT PRIMARY KEY,
    patient_id INT NOT NULL,
    doctor_id INT NOT NULL,
    appointment_date TIMESTAMP NOT NULL,
    status VARCHAR(20) DEFAULT 'CONFIRMED',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_id) REFERENCES patients(patient_id) ON DELETE CASCADE,
    FOREIGN KEY (doctor_id) REFERENCES doctors(doctor_id) ON DELETE CASCADE
);
```
**Indices**: PRIMARY KEY (appointment_id), FOREIGN KEYs (patient_id, doctor_id)
**Status Values**: CONFIRMED, CANCELLED
**Relationships**: Links to patients and doctors tables

---

## Core Features

### Patient Portal

1. **Register**
   - Enter: name, email, phone, age, gender, address
   - Validation: Email must be unique, all fields required
   - Result: Patient ID assigned by database

2. **Login**
   - Enter: email
   - Validation: Email must exist in database
   - Result: Patient object loaded

3. **Request Appointment**
   - Enter: specialization (Cardiology, Neurology, General, Pediatrics), date, optional description
   - Validation: Date must be valid, specialization must match available doctors
   - Result: Appointment request created with PENDING status

4. **View Request Status**
   - Display: All appointment requests for logged-in patient
   - Shows: Request ID, specialization, requested date, current status (PENDING/APPROVED)

5. **View Confirmed Appointments**
   - Display: All confirmed appointments for logged-in patient
   - Shows: Appointment ID, doctor name, appointment date, status

### Admin Portal

1. **View Pending Requests**
   - Display: All appointment requests with status=PENDING
   - Shows: Request ID, patient name, specialization, requested date, description

2. **View Available Doctors**
   - Display: All doctors where available=TRUE
   - Optional filter: By specialization
   - Shows: Doctor ID, name, specialization, email, phone, qualification, experience

3. **Fix Appointment (Assign Doctor to Request)**
   - Select: Pending appointment request
   - Select: Available doctor matching specialization
   - Validation: 
     - Doctor specialization must match request specialization
     - Doctor must be available
     - Doctor must not have conflict on requested date
   - Result: Appointment created (appointment_requests status updated to APPROVED)

4. **Manage Doctors**
   - Add Doctor: Enter name, specialization, email, phone, qualification, experience
   - View All Doctors: Display complete doctor list with availability status
   - Update Doctor: Modify availability status

5. **View All Appointments**
   - Display: All confirmed appointments in system
   - Shows: Appointment ID, patient name, doctor name, appointment date, status

---

## Project Structure

```
hospital_management/
├── pom.xml                                  # Maven POM configuration
│   ├── Dependencies:
│   │   ├── mysql-connector-j:8.0.33
│   │   └── junit:4.13.2 (test scope)
│   └── Plugins:
│       ├── maven-compiler-plugin (Java 11)
│       └── exec-maven-plugin (mainClass: com.hospital.Main)
│
├── src/
│   ├── main/
│   │   ├── java/com/hospital/
│   │   │   ├── Main.java                         # Entry point
│   │   │   │   └── Routes: Patient/Admin/Exit
│   │   │   │
│   │   │   ├── dao/                             # Data Access Layer
│   │   │   │   ├── PatientDAO.java              # Patient CRUD + custom queries
│   │   │   │   ├── DoctorDAO.java               # Doctor CRUD + specialization queries
│   │   │   │   ├── AppointmentDAO.java          # Appointment CRUD
│   │   │   │   └── AppointmentRequestDAO.java   # Request CRUD
│   │   │   │
│   │   │   ├── model/                           # Entity Classes (POJOs)
│   │   │   │   ├── Patient.java                 # Fields: id, name, email, phone, age, gender, address
│   │   │   │   ├── Doctor.java                  # Fields: id, name, specialization, email, phone, qualification, experience, available
│   │   │   │   ├── Appointment.java             # Fields: id, patientId, doctorId, date, status
│   │   │   │   └── AppointmentRequest.java      # Fields: id, patientId, specialization, date, status, description
│   │   │   │
│   │   │   ├── service/                         # Business Logic Layer
│   │   │   │   ├── PatientService.java          # Patient validation & operations
│   │   │   │   ├── DoctorService.java           # Doctor management logic
│   │   │   │   └── AppointmentService.java      # Appointment scheduling logic
│   │   │   │
│   │   │   ├── ui/                              # Presentation Layer
│   │   │   │   ├── PatientUI.java               # Patient menu & interactions
│   │   │   │   └── AdminUI.java                 # Admin menu & interactions
│   │   │   │
│   │   │   └── util/
│   │   │       └── DatabaseConnection.java      # JDBC connection management
│   │   │           └── Loads: db.properties
│   │   │
│   │   └── resources/
│   │       ├── database_schema.sql              # SQL setup script (60+ lines)
│   │       │   ├── CREATE DATABASE hospital_db
│   │       │   ├── CREATE 4 tables
│   │       │   └── INSERT sample data
│   │       │
│   │       └── db.properties                    # Database configuration
│   │           ├── db.url=jdbc:mysql://localhost:3306/hospital_db?...
│   │           ├── db.username=root
│   │           └── db.password=root@123
│   │
│   └── test/                                    # Test resources (if applicable)
│
├── target/                                      # Build output directory
│   ├── classes/
│   │   ├── com/hospital/                       # Compiled classes
│   │   ├── database_schema.sql                 # Copied from resources
│   │   └── db.properties                       # Copied from resources
│   ├── hospital-management-system-1.0.0.jar    # JAR artifact
│   └── maven-archiver/
│       └── pom.properties
│
├── README.md                                    # Quick start guide
├── PROJECT_OVERVIEW.md                         # Project objectives & overview
├── ARCHITECTURE.md                              # Detailed architecture explanation
├── SETUP.md                                     # Setup instructions
├── HOW_TO_RUN.md                                # Run instructions
└── run.bat                                      # Windows batch script to run app
```

**Total Files**: 15 Java source files + SQL schema + configuration files

---

## Runtime Flow

### Step-by-Step Execution

1. **Application Start**
   ```
   java -cp target/classes com.hospital.Main
   ```

2. **Main.main() Execution**
   ```
   - Displays menu: "1. Patient Portal", "2. Admin Portal", "3. Exit"
   - Creates Scanner for user input
   - Reads user choice (1, 2, or 3)
   ```

3. **User Choice: Patient Portal (1)**
   ```
   - Creates PatientUI instance
   - Calls patientUI.start()
   - Displays patient menu: Register, Login, Request Appointment, View Status, etc.
   - Each option calls corresponding PatientService method
   - PatientService calls PatientDAO for database operations
   ```

4. **Database Connection Process**
   ```
   - When data access needed:
     a. DAO method creates connection: DatabaseConnection.getConnection()
     b. loadProperties() loads db.properties from classpath
     c. Properties contain: db.url, db.username, db.password, db.driver
     d. DriverManager.getConnection(url, username, password)
     e. Returns Connection object
   ```

5. **SQL Query Execution Example (Patient Registration)**
   ```
   User Input → PatientUI.register() 
             → PatientService.registerPatient(patient)
             → Validation (name, email, phone required; email unique)
             → PatientDAO.addPatient(patient)
             → SQL: INSERT INTO patients (name, email, phone, age, gender, address) 
                    VALUES (?, ?, ?, ?, ?, ?)
             → PreparedStatement executes with bound parameters
             → getGeneratedKeys() retrieves new patient_id
             → Returns patientId to UI for confirmation
   ```

6. **Result Display**
   ```
   - DAO returns results to Service
   - Service validates/processes results
   - UI displays formatted output to user
   - User continues with menu options or exits
   ```

7. **Resource Cleanup**
   ```
   - Try-with-resources automatically closes:
     - ResultSet
     - PreparedStatement
     - Connection
   - No manual cleanup needed
   ```

### Exception Handling Flow
```
SQL Exception → DAO catches → Logs error → Returns -1 or null
             → Service handles → Throws RuntimeException or IllegalArgumentException
             → UI catches → Displays error message to user
             → Application continues or exits gracefully
```

---

## Configuration

### Database Configuration File (`db.properties`)

Located at: `src/main/resources/db.properties`

```properties
# Database Configuration
db.url=jdbc:mysql://localhost:3306/hospital_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
db.username=root
db.password=root@123
db.driver=com.mysql.cj.jdbc.Driver
```

**URL Parameters Explained:**
- `localhost:3306` - MySQL server address and default port
- `hospital_db` - Database name
- `useSSL=false` - Disable SSL connection (for local development)
- `allowPublicKeyRetrieval=true` - Allow public key retrieval for authentication
- `serverTimezone=UTC` - Set server timezone to UTC

**Connection Method:**
```
Properties loads → DriverManager.getConnection(url, username, password)
                → New Connection object returned
                → Try-with-resources closes after use
```

### Compiler Configuration (`pom.xml`)

```xml
<properties>
    <maven.compiler.source>11</maven.compiler.source>
    <maven.compiler.target>11</maven.compiler.target>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
</properties>
```

**Meaning:**
- Code compiled for Java 11
- Source and target compatibility: Java 11
- File encoding: UTF-8 (supports all characters)

---

## Build & Run Commands

### Prerequisites Check
```batch
where mvn          # Find Maven installation
where java         # Find Java installation
```

### Build Only (No Execution)
```bash
mvn -DskipTests clean package
```
**Output**: `target/hospital-management-system-1.0.0.jar` and compiled classes in `target/classes/`

### Build and Run (Interactive)
```bash
mvn -DskipTests compile exec:java -Dexec.mainClass=com.hospital.Main
```

### Run Compiled Classes (Direct Java)
```bash
java -cp target/classes com.hospital.Main
```

### Run JAR File
```bash
java -jar target/hospital-management-system-1.0.0.jar
```

### Run Non-Interactively (Pipe Input)
```bash
echo 3 | java -cp target/classes com.hospital.Main
# This pipes "3" (Exit) to the main class, exits immediately
```

### Using run.bat (Windows)
```batch
run.bat
```
Executes: `mvn clean compile exec:java`

---

## Design Patterns

### 1. Three-Tier Architecture Pattern
- **Presentation Layer**: User interface and input handling
- **Business Logic Layer**: Rules, validation, coordination
- **Data Access Layer**: Database operations encapsulation

**Benefits**: Separation of concerns, testability, maintainability, scalability

### 2. DAO (Data Access Object) Pattern
- Encapsulates all SQL queries in DAO classes
- Service layer never directly uses JDBC
- Model objects insulated from database details

**Benefits**: Easier to change database, reuse queries, unit testing

### 3. Model-View-Controller (MVC) Inspired
- **Model**: Patient, Doctor, Appointment, AppointmentRequest (POJOs)
- **View**: PatientUI, AdminUI (console-based)
- **Controller**: Service layer (business logic)

**Benefits**: Clear separation of concerns, maintainable, testable

### 4. Resource Management Pattern (Try-with-Resources)
```java
try (Connection conn = DatabaseConnection.getConnection();
     PreparedStatement stmt = conn.prepareStatement(sql)) {
    // Use resources
} catch (SQLException e) {
    // Handle error
}
// Automatic cleanup: stmt.close(), conn.close()
```

**Benefits**: Prevents resource leaks, cleaner code, automatic exception suppression

### 5. Prepared Statements (SQL Injection Prevention)
```java
String sql = "SELECT * FROM patients WHERE email = ?";
PreparedStatement stmt = conn.prepareStatement(sql);
stmt.setString(1, email);  // Parameter binding
ResultSet rs = stmt.executeQuery();
```

**Benefits**: SQL injection prevention, parameter escaping, query caching

### 6. Singleton-Like Pattern (DatabaseConnection)
- Static initialization block for driver registration
- Static method `getConnection()` provides centralized access
- Properties loaded once and reused

**Benefits**: Centralized configuration, consistent connection creation

### 7. Service Locator Pattern (Limited)
- Services instantiate DAOs internally
- UI calls services, not DAOs directly
- Provides single entry point for business logic

**Benefits**: Abstraction, easier to mock/test, centralized business rules

### 8. Validation Pattern
- Input validation at Service layer (not UI or DAO)
- Prevents invalid data from reaching database
- Specific exception messages for different validation failures

**Benefits**: Consistent validation, reusable across UIs, data integrity

---

## Dependencies

### Maven POM Configuration (`pom.xml`)

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.hospital</groupId>
    <artifactId>hospital-management-system</artifactId>
    <version>1.0.0</version>
    <packaging>jar</packaging>

    <name>Hospital Management System</name>
    <description>Three-tier Architecture Hospital Management System</description>

    <properties>
        <maven.compiler.source>11</maven.compiler.source>
        <maven.compiler.target>11</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>

    <dependencies>
        <!-- MySQL Connector/J JDBC Driver -->
        <dependency>
            <groupId>com.mysql</groupId>
            <artifactId>mysql-connector-j</artifactId>
            <version>8.0.33</version>
            <scope>runtime</scope>
        </dependency>

        <!-- JUnit for Unit Testing -->
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.13.2</version>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <!-- Maven Compiler Plugin -->
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.8.1</version>
                <configuration>
                    <source>11</source>
                    <target>11</target>
                </configuration>
            </plugin>

            <!-- Maven Exec Plugin (Run Java Application) -->
            <plugin>
                <groupId>org.codehaus.mojo</groupId>
                <artifactId>exec-maven-plugin</artifactId>
                <version>3.1.0</version>
                <configuration>
                    <mainClass>com.hospital.Main</mainClass>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```

### Dependency Details

#### 1. MySQL Connector/J (8.0.33)
- **GroupId**: com.mysql
- **ArtifactId**: mysql-connector-j
- **Version**: 8.0.33
- **Scope**: runtime
- **Purpose**: JDBC driver for MySQL connectivity
- **Usage**: Loaded via `Class.forName("com.mysql.cj.jdbc.Driver")`

#### 2. JUnit (4.13.2)
- **GroupId**: junit
- **ArtifactId**: junit
- **Version**: 4.13.2
- **Scope**: test
- **Purpose**: Unit testing framework
- **Usage**: Not currently used in project, available for future test cases

### Build Plugins

#### 1. Maven Compiler Plugin (3.8.1)
- **Configuration**: Java 11 source and target
- **Purpose**: Compile Java source code to bytecode
- **Execution**: `mvn clean compile`

#### 2. Maven Exec Plugin (3.1.0)
- **Configuration**: Executes `com.hospital.Main`
- **Purpose**: Run Java application from Maven
- **Execution**: `mvn exec:java`

---

## Additional Information

### Sample Data Pre-loaded

**Patients (3 records):**
- John Doe (john.doe@email.com)
- Jane Smith (jane.smith@email.com)
- Bob Johnson (bob.johnson@email.com)

**Doctors (5 records):**
- Dr. Sarah Williams (Cardiology, 10 years experience)
- Dr. Michael Brown (Neurology, 15 years experience)
- Dr. Emily Davis (General Medicine, 8 years experience)
- Dr. James Wilson (Cardiology, 12 years experience)
- Dr. Lisa Anderson (Pediatrics, 7 years experience)

### Common Use Cases

1. **Patient Registration**
   - Flow: PatientUI → PatientService → PatientDAO → INSERT query → Database
   - Validation: Email unique check

2. **Appointment Request**
   - Flow: PatientUI → AppointmentService → AppointmentRequestDAO → INSERT query
   - Validation: Specialization match with available doctors

3. **Appointment Assignment (Admin)**
   - Flow: AdminUI → AppointmentService → AppointmentDAO (INSERT) + AppointmentRequestDAO (UPDATE)
   - Validation: Doctor specialization match, doctor availability, date availability

4. **Doctor Management (Admin)**
   - Flow: AdminUI → DoctorService → DoctorDAO → INSERT/SELECT/UPDATE queries
   - Validation: Email unique check, required fields

---

## Troubleshooting

### Issue: MySQL Connection Failed
**Cause**: Database not running, wrong credentials, or wrong URL
**Solution**: 
1. Verify MySQL is running: `mysql -u root -p`
2. Check `db.properties` for correct URL, username, password
3. Run database schema: `mysql -u root -p < src/main/resources/database_schema.sql`

### Issue: Scanner throws NoSuchElementException
**Cause**: No input provided to Scanner (typically when running non-interactively)
**Solution**: Pipe input or run interactively: `echo 3 | java -cp target/classes com.hospital.Main`

### Issue: Maven not found
**Cause**: Maven not installed or not in PATH
**Solution**: Install Maven from https://maven.apache.org/download.cgi

### Issue: Java version mismatch
**Cause**: Installed Java version differs from Java 11
**Solution**: Install Java 11 or update `pom.xml` properties to match installed version

---

## Future Enhancement Ideas

1. **Add User Authentication**: Secure login with passwords (currently email-based)
2. **Add Email Notifications**: Notify patients/doctors of appointment confirmations
3. **Add Appointment Rescheduling**: Allow changes to existing appointments
4. **Add Doctor Schedules**: Track available time slots, not just date
5. **Add Payment Module**: Process appointment payments
6. **Add RESTful API**: Expose services via HTTP endpoints
7. **Add Web UI**: Replace console UI with web interface using Spring Boot
8. **Add ORM**: Use Hibernate/JPA instead of raw JDBC
9. **Add Unit Tests**: Comprehensive test coverage using JUnit/Mockito
10. **Add Logging**: Implement SLF4J for better logging

---

## License & Credits

This is an educational project demonstrating three-tier architecture, JDBC usage, and DAO pattern implementation in Java.

---

**Documentation Generated**: November 15, 2025
**Project Version**: 1.0.0
**Java Version**: 11+
