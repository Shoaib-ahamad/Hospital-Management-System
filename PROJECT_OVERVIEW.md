# Hospital Management System — Project Overview

This document explains what the Hospital Management System does, how it works, the technologies it uses, and how to run and extend it. Copy this into your project files (README or docs) as needed.

---

## 1. Project Objectives

- Provide a simple command-line hospital management system demonstrating a three-tier architecture (UI → Service → DAO) using Java and MySQL.
- Allow patients to register, login, request appointments, and view appointments.
- Allow administrators to manage doctors and appointments.
- Serve as an educational example of JDBC usage, DAO pattern, and small-scale CRUD workflows.

## 2. Key Features

- Patient portal
  - Register a new patient
  - Login with email
  - Submit appointment requests
  - View appointments

- Admin portal
  - Add / update / list doctors
  - View and manage appointment requests and appointments

- Persistence layer using MySQL (JDBC)
- Simple console UI (text-based) suitable for demonstration and learning

## 3. High-level Architecture

- UI layer (`com.hospital.ui`)
  - `Main` starts the app and shows the main menu
  - `PatientUI`, `AdminUI` contain interactive menus and collect user input

- Service layer (`com.hospital.service`)
  - `PatientService`, `DoctorService`, `AppointmentService`
  - Implements business rules and coordinates DAO calls

- DAO / Persistence (`com.hospital.dao`)
  - `PatientDAO`, `DoctorDAO`, `AppointmentDAO`, `AppointmentRequestDAO`
  - Encapsulate JDBC code (queries, result mapping)

- Utilities (`com.hospital.util`)
  - `DatabaseConnection` loads `db.properties` and returns a JDBC `Connection`

- Model objects (`com.hospital.model`)
  - `Patient`, `Doctor`, `Appointment`, `AppointmentRequest`

Directory layout (relevant files):
```
src/main/java/com/hospital/
  Main.java
  dao/
  model/
  service/
  ui/
  util/DatabaseConnection.java
src/main/resources/
  db.properties
  database_schema.sql
pom.xml
```

## 4. Database Schema (summary)

The SQL schema is available in `src/main/resources/database_schema.sql`. Main tables:

- `patients` (patient_id, name, email (unique), phone, age, gender, address)
- `doctors` (doctor_id, name, email (unique), specialization, phone, qualification, experience, available)
- `appointment_requests` (request_id, patient_id → patients.patient_id, specialization, requested_date, status)
- `appointments` (appointment_id, patient_id, doctor_id, appointment_date, status)

Refer to the SQL file for full CREATE TABLE statements and constraints.

## 5. Technologies Used

- Java (JDK 11+; code compiled for Java 11 in pom.xml)
- Maven (build & dependency management)
- MySQL (persistent datastore)
- JDBC (MySQL Connector/J)
- JUnit (test dependency available)

## 6. How it works (runtime flow)

1. App starts (`Main.main`) and shows a text menu.
2. When DB is needed, `DatabaseConnection.getConnection()` loads `db.properties` from the classpath and returns a JDBC Connection.
3. UI classes call corresponding Service classes.
4. Service classes call DAO classes. DAOs open/close JDBC resources (Connections, Statements, ResultSets) and map rows to model objects.
5. Results are returned and displayed to the user via the console UI.

## 7. Configuration

Database connection is driven by `src/main/resources/db.properties`. Example values:

```
# Database Configuration
db.url=jdbc:mysql://localhost:3306/hospital_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
db.username=root
db.password=<your-db-password>
db.driver=com.mysql.cj.jdbc.Driver
```

Notes:
- If you change `db.properties`, re-run `mvn clean install` (or otherwise ensure the resource is copied to `target/classes`) before running the app so the runtime classpath picks up the change.
- Avoid placing secrets in source control for real projects. Use environment variables, external config files outside the repo, or a secrets manager in production.

## 8. Run Instructions (Windows CMD)

1. Make sure MySQL server is running and accessible.
2. Create the database and tables (if not present):

```cmd
mysql -u root -p < src\main\resources\database_schema.sql
```

3. Build the project (downloads dependencies and compiles):

```cmd
mvn clean install
```

4. Run the application (console):

```cmd
mvn exec:java -Dexec.mainClass="com.hospital.Main"
```

5. Use the displayed menu to choose Patient Portal (1) or Admin Portal (2).

Tip: if you update `db.properties` edit `src/main/resources/db.properties` and re-run `mvn clean install` so `target/classes/db.properties` is updated.

## 9. Troubleshooting

- Error: "Access denied for user 'root'@'localhost'"
  - Confirm the credentials in `src/main/resources/db.properties` match your MySQL user/password.
  - Test with:
    ```cmd
    mysql -u root -p
    ```
  - If connecting works from CLI but the app still fails, ensure you rebuilt the project and that `target/classes/db.properties` contains your updated password.

- Error: "Public Key Retrieval is not allowed"
  - Add `allowPublicKeyRetrieval=true` to the JDBC URL (this repo already includes it). Example:
    ```text
    jdbc:mysql://localhost:3306/hospital_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
    ```

- Error: "Cannot invoke java.sql.Connection.prepareStatement because this.connection is null"
  - Means the DAO attempted to use a connection before it was established. Ensure `DatabaseConnection.getConnection()` is returning a non-null connection and no exceptions occurred during connection creation. Check earlier error messages in the console for the root cause.

- If you see stale values after editing `db.properties`:
  - Re-run `mvn clean install` so updated resources are copied to `target/classes`.

## 10. Contract / Inputs & Outputs

- Inputs:
  - Console input (menus, strings, integers, dates)
  - Database persistence (JDBC parameters in `db.properties`)

- Outputs:
  - Console output for menus and operation results
  - Database records in MySQL tables

- Error modes:
  - Invalid user input → program asks again or prints error
  - DB auth/connectivity failure → stack trace logged and app may terminate or display an error
  - SQL constraint failures (duplicate email) → DAO surfaces error which service / UI should handle

Success criteria for operations:
- Register: patient created in `patients` table, insert returns generated id
- Login: patient is retrieved by email and verified to exist

## 11. Edge cases & recommendations

- Edge cases
  - Duplicate emails on registration (enforced unique constraint)
  - Invalid date formats for appointments
  - Missing required fields (name, email, phone)

- Improvements (next steps)
  - Use connection pooling (HikariCP) for better performance
  - Move configuration to environment variables or a secure external config
  - Add unit and integration tests (use an embedded database like H2 for tests)
  - Add input validation and stronger error handling in Service/UI layers
  - Introduce DB migrations (Flyway or Liquibase) to manage schema changes
  - Replace console UI with a web or REST UI for usability

## 12. Quick reference — useful commands

```cmd
# Load DB schema
mysql -u root -p < src\main\resources\database_schema.sql

# Build
mvn clean install

# Run
mvn exec:java -Dexec.mainClass="com.hospital.Main"
```

## 13. Files to inspect when debugging

- `src/main/resources/db.properties` — DB connection values
- `src/main/resources/database_schema.sql` — DB schema
- `src/main/java/com/hospital/util/DatabaseConnection.java` — how the app loads db.properties and creates connections
- `src/main/java/com/hospital/dao/*` — JDBC/SQL queries
- `src/main/java/com/hospital/ui/*` — console menus and input handling

---

If you want, I can:
- Add this file into your repo (already done as `PROJECT_OVERVIEW.md`), or
- Insert a condensed version into `README.md`, or
- Generate a `docs/` folder with separate smaller documents (Architecture, Run Guide, DB, Troubleshooting).

Tell me which option you prefer and I will proceed.
