# How to Run the Hospital Management System

## Prerequisites Check

Before running, ensure you have:
- ✅ **Java 11 or higher** installed
- ✅ **Maven** installed
- ✅ **MySQL** installed and running

### Check Java Installation
```bash
java -version
```

### Check Maven Installation
```bash
mvn -version
```

### Check MySQL Installation
```bash
mysql --version
```

---

## Step-by-Step Instructions

### Step 1: Setup MySQL Database

1. **Start MySQL Server**
   - On Windows: Start MySQL service from Services or use MySQL Workbench
   - On Linux/Mac: `sudo systemctl start mysql` or `brew services start mysql`

2. **Create Database and Tables**
   
   **Option A: Using Command Line**
   ```bash
   mysql -u root -p < src/main/resources/database_schema.sql
   ```
   (Enter your MySQL password when prompted)

   **Option B: Using MySQL Workbench**
   - Open MySQL Workbench
   - Connect to your MySQL server
   - Open `src/main/resources/database_schema.sql`
   - Execute the script (Ctrl+Shift+Enter or click Execute)

   **Option C: Manual Execution**
   - Open MySQL command line: `mysql -u root -p`
   - Copy and paste the contents of `src/main/resources/database_schema.sql`
   - Press Enter

3. **Verify Database Created**
   ```sql
   USE hospital_db;
   SHOW TABLES;
   ```
   You should see: `patients`, `doctors`, `appointments`, `appointment_requests`

### Step 2: Configure Database Connection

1. **Open** `src/main/java/com/hospital/util/DatabaseConnection.java`

2. **Update** the following if your MySQL settings are different:
   ```java
   private static final String USERNAME = "root";  // Change if different
   private static final String PASSWORD = "root";  // Change to your MySQL password
   ```

3. **Save** the file

### Step 3: Download Dependencies

Open terminal/command prompt in the project root directory and run:

```bash
mvn clean install
```

This will download MySQL connector and other dependencies.

### Step 4: Run the Application

#### **Method 1: Using Maven (Recommended)**

```bash
mvn exec:java -Dexec.mainClass="com.hospital.Main"
```

#### **Method 2: Using Maven (Compile then Run)**

```bash
# Compile
mvn clean compile

# Run
mvn exec:java -Dexec.mainClass="com.hospital.Main"
```

#### **Method 3: Using IDE (IntelliJ IDEA / Eclipse / VS Code)**

**IntelliJ IDEA:**
1. Open the project folder
2. Wait for Maven to import dependencies
3. Right-click on `src/main/java/com/hospital/Main.java`
4. Select "Run 'Main.main()'"

**Eclipse:**
1. File → Import → Maven → Existing Maven Projects
2. Select the project folder
3. Right-click `Main.java` → Run As → Java Application

**VS Code:**
1. Install Java Extension Pack
2. Open the project folder
3. Open `Main.java`
4. Click "Run" button or press F5

#### **Method 4: Manual Compilation and Execution**

```bash
# Compile
javac -cp "target/classes:$(mvn dependency:build-classpath -q -Dmdep.outputFile=/dev/stdout)" src/main/java/com/hospital/**/*.java -d target/classes

# Run (simpler with Maven)
mvn exec:java -Dexec.mainClass="com.hospital.Main"
```

---

## Running the Application

Once the application starts, you'll see:

```
========================================
   HOSPITAL MANAGEMENT SYSTEM
========================================
1. Patient Portal
2. Admin Portal
3. Exit
Choose an option:
```

### Testing as Patient:

1. **Select option 1** (Patient Portal)
2. **Register** as a new patient:
   - Choose option 1 (Register)
   - Enter your details (name, email, phone, age, gender, address)
   - Note your Patient ID
3. **Request Appointment**:
   - Choose option 1 (Request Doctor Appointment)
   - Enter specialization (e.g., `Cardiology`, `Neurology`, `General`)
   - Enter date in format: `yyyy-MM-dd` (e.g., `2024-12-25`)
   - Enter optional description
4. **View Requests**: Choose option 2 to see your appointment requests
5. **View Appointments**: Choose option 3 to see confirmed appointments

### Testing as Admin:

1. **Select option 2** (Admin Portal)
2. **Enter password**: `admin123`
3. **View Pending Requests**: Choose option 1
4. **View Available Doctors**: Choose option 2
   - Enter specialization to filter (or press Enter for all)
5. **Fix Appointment**: Choose option 3
   - Enter Request ID from pending requests
   - Enter Doctor ID from available doctors
   - System will validate and create appointment
6. **View All Appointments**: Choose option 4

---

## Troubleshooting

### ❌ Error: "MySQL JDBC Driver not found!"

**Solution:**
```bash
mvn clean install
```
This downloads the MySQL connector dependency.

### ❌ Error: "Connection failed! Check output console"

**Possible causes:**
1. MySQL server is not running
   - **Fix**: Start MySQL service
2. Wrong username/password
   - **Fix**: Update `DatabaseConnection.java` with correct credentials
3. Database doesn't exist
   - **Fix**: Run the SQL schema script again
4. Wrong port
   - **Fix**: If MySQL is on different port, update URL in `DatabaseConnection.java`

### ❌ Error: "Access denied for user"

**Solution:**
- Check MySQL username and password in `DatabaseConnection.java`
- Ensure MySQL user has privileges to create databases

### ❌ Error: "Could not find or load main class"

**Solution:**
```bash
mvn clean compile
mvn exec:java -Dexec.mainClass="com.hospital.Main"
```

### ❌ Error: "Package does not exist"

**Solution:**
```bash
mvn clean install
```

### ❌ Maven command not found

**Solution:**
- Install Maven: https://maven.apache.org/download.cgi
- Add Maven to PATH environment variable

---

## Quick Test Script

After setup, you can quickly test the flow:

1. **Start Application**: `mvn exec:java -Dexec.mainClass="com.hospital.Main"`
2. **As Patient**: Register → Request appointment (Cardiology, 2024-12-25)
3. **As Admin**: Login (admin123) → View requests → Fix appointment

---

## Sample Data

The database schema includes sample data:
- **3 Sample Patients**: john.doe@email.com, jane.smith@email.com, bob.johnson@email.com
- **5 Sample Doctors**: 
  - Dr. Sarah Williams (Cardiology)
  - Dr. Michael Brown (Neurology)
  - Dr. Emily Davis (General)
  - Dr. James Wilson (Cardiology)
  - Dr. Lisa Anderson (Pediatrics)

You can use these for testing or create new ones.

---

## Need Help?

If you encounter issues:
1. Check MySQL is running: `mysql -u root -p`
2. Verify database exists: `SHOW DATABASES;`
3. Check tables: `USE hospital_db; SHOW TABLES;`
4. Review error messages in console
5. Ensure all prerequisites are installed correctly

