# Quick Setup Guide

## Step 1: Install Prerequisites

1. **Java 11+**: Download and install from [Oracle](https://www.oracle.com/java/technologies/downloads/) or [OpenJDK](https://openjdk.org/)
2. **Maven**: Download and install from [Apache Maven](https://maven.apache.org/download.cgi)
3. **MySQL**: Download and install from [MySQL](https://dev.mysql.com/downloads/)

## Step 2: Setup Database

1. Start MySQL server
2. Open MySQL command line or MySQL Workbench
3. Run the SQL script:
   ```bash
   mysql -u root -p < src/main/resources/database_schema.sql
   ```
   Or copy and paste the contents of `src/main/resources/database_schema.sql` into MySQL Workbench

## Step 3: Configure Database Connection

Edit `src/main/java/com/hospital/util/DatabaseConnection.java` and update:
- `USERNAME`: Your MySQL username (default: `root`)
- `PASSWORD`: Your MySQL password (default: `root`)

## Step 4: Build and Run

### Option 1: Using Maven
```bash
# Compile
mvn clean compile

# Run
mvn exec:java -Dexec.mainClass="com.hospital.Main"
```

### Option 2: Using IDE
1. Import project as Maven project
2. Run `com.hospital.Main` class

## Step 5: Test the Application

### As Patient:
1. Select option 1 (Patient Portal)
2. Register with your details
3. Request an appointment (e.g., Cardiology, 2024-12-25)
4. View your requests

### As Admin:
1. Select option 2 (Admin Portal)
2. Enter password: `admin123`
3. View pending requests
4. View available doctors
5. Fix appointment by assigning a doctor to a request

## Troubleshooting

### Database Connection Error
- Ensure MySQL server is running
- Check username and password in `DatabaseConnection.java`
- Verify database `hospital_db` exists

### ClassNotFoundException for MySQL Driver
- Run `mvn clean install` to download dependencies
- Check `pom.xml` has MySQL connector dependency

### Port Already in Use
- Check if MySQL is running on port 3306
- Update connection URL if using different port

