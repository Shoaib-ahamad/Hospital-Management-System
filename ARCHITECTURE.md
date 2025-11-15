# Three-Tier Architecture Overview

## Architecture Diagram

```
┌─────────────────────────────────────────────────────────┐
│              PRESENTATION LAYER (UI)                    │
│  ┌──────────────┐          ┌──────────────┐            │
│  │  PatientUI   │          │   AdminUI    │            │
│  └──────────────┘          └──────────────┘            │
│         │                          │                    │
└─────────┼──────────────────────────┼────────────────────┘
          │                          │
          ▼                          ▼
┌─────────────────────────────────────────────────────────┐
│           BUSINESS LOGIC LAYER (Service)                │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐ │
│  │PatientService│  │DoctorService │  │Appointment   │ │
│  │              │  │              │  │Service       │ │
│  └──────────────┘  └──────────────┘  └──────────────┘ │
│         │                  │                  │         │
└─────────┼──────────────────┼──────────────────┼────────┘
          │                  │                  │
          ▼                  ▼                  ▼
┌─────────────────────────────────────────────────────────┐
│          DATA ACCESS LAYER (DAO)                        │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐ │
│  │  PatientDAO  │  │  DoctorDAO   │  │ Appointment  │ │
│  │              │  │              │  │    DAO       │ │
│  └──────────────┘  └──────────────┘  └──────────────┘ │
│  ┌──────────────────────────────────────────────┐      │
│  │      AppointmentRequestDAO                    │      │
│  └──────────────────────────────────────────────┘      │
│         │                  │                  │         │
└─────────┼──────────────────┼──────────────────┼────────┘
          │                  │                  │
          ▼                  ▼                  ▼
┌─────────────────────────────────────────────────────────┐
│                    DATABASE (MySQL)                     │
│  patients | doctors | appointments | appointment_requests│
└─────────────────────────────────────────────────────────┘
```

## Layer Responsibilities

### 1. Presentation Layer (`com.hospital.ui`)
**Purpose**: User interface and interaction

**Components**:
- `Main.java`: Application entry point, routes to Patient or Admin portal
- `PatientUI.java`: Handles all patient interactions
  - Registration/Login
  - Request appointment
  - View requests and appointments
- `AdminUI.java`: Handles all admin interactions
  - View pending requests
  - View available doctors
  - Fix appointments
  - Manage doctors

**Responsibilities**:
- Display menus and options
- Collect user input
- Call service layer methods
- Display results to users
- Handle user flow and navigation

### 2. Business Logic Layer (`com.hospital.service`)
**Purpose**: Business rules and validation

**Components**:
- `PatientService.java`: Patient business logic
  - Validates patient data
  - Checks for duplicate emails
  - Manages patient operations
- `DoctorService.java`: Doctor business logic
  - Validates doctor data
  - Manages doctor operations
- `AppointmentService.java`: Appointment business logic
  - Validates appointment requests
  - Checks date validity
  - Manages appointment creation
  - Validates doctor availability and specialization match

**Responsibilities**:
- Implement business rules
- Validate input data
- Coordinate between multiple DAOs
- Handle business logic exceptions
- Ensure data integrity

### 3. Data Access Layer (`com.hospital.dao`)
**Purpose**: Database operations

**Components**:
- `PatientDAO.java`: Patient database operations
- `DoctorDAO.java`: Doctor database operations
- `AppointmentDAO.java`: Appointment database operations
- `AppointmentRequestDAO.java`: Appointment request database operations

**Responsibilities**:
- Execute SQL queries
- Map database results to model objects
- Handle database connections
- Manage transactions (basic implementation)
- Abstract database details from business layer

## Data Flow Example: Patient Requests Appointment

1. **Presentation Layer** (`PatientUI.requestAppointment()`)
   - User enters specialization and date
   - Collects input from console

2. **Business Logic Layer** (`AppointmentService.requestAppointment()`)
   - Validates patient exists
   - Validates specialization is provided
   - Validates date is not in the past
   - Creates AppointmentRequest object

3. **Data Access Layer** (`AppointmentRequestDAO.addAppointmentRequest()`)
   - Executes INSERT SQL query
   - Returns generated request ID

4. **Response flows back**:
   - DAO returns request ID
   - Service returns request ID
   - UI displays success message

## Data Flow Example: Admin Fixes Appointment

1. **Presentation Layer** (`AdminUI.fixAppointment()`)
   - Shows pending requests
   - Shows available doctors
   - Collects request ID and doctor ID

2. **Business Logic Layer** (`AppointmentService.fixAppointment()`)
   - Validates request exists and is pending
   - Validates doctor exists and is available
   - Validates specialization match
   - Checks doctor availability on requested date
   - Creates appointment
   - Updates request status

3. **Data Access Layer**:
   - `AppointmentRequestDAO.getRequestById()`: Get request
   - `DoctorDAO.getDoctorById()`: Get doctor
   - `AppointmentDAO.checkDoctorAvailability()`: Check date availability
   - `AppointmentDAO.addAppointment()`: Create appointment
   - `AppointmentRequestDAO.updateRequestStatus()`: Update request

4. **Response flows back**:
   - DAO returns appointment ID
   - Service returns appointment ID
   - UI displays success message

## Benefits of Three-Tier Architecture

1. **Separation of Concerns**: Each layer has a specific responsibility
2. **Maintainability**: Changes in one layer don't affect others
3. **Scalability**: Easy to replace UI (e.g., web interface) without changing business logic
4. **Testability**: Each layer can be tested independently
5. **Reusability**: Business logic can be reused by different UIs
6. **Flexibility**: Database can be changed without affecting business logic

## Model Layer (`com.hospital.model`)

**Purpose**: Data structures (entities)

**Components**:
- `Patient.java`: Patient entity
- `Doctor.java`: Doctor entity
- `Appointment.java`: Appointment entity
- `AppointmentRequest.java`: Appointment request entity

**Responsibilities**:
- Represent data structures
- Provide getters and setters
- No business logic (POJOs)

## Utility Layer (`com.hospital.util`)

**Purpose**: Shared utilities

**Components**:
- `DatabaseConnection.java`: Database connection management

**Responsibilities**:
- Provide reusable utility functions
- Manage database connections
- Handle connection pooling (basic implementation)

