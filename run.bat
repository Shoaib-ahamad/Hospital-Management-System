@echo off
echo ========================================
echo   Hospital Management System
echo ========================================
echo.
echo Checking prerequisites...
echo.

REM Check if Maven is installed
where mvn >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Maven is not installed or not in PATH
    echo Please install Maven from https://maven.apache.org/download.cgi
    pause
    exit /b 1
)

REM Check if Java is installed
where java >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Java is not installed or not in PATH
    echo Please install Java 11 or higher
    pause
    exit /b 1
)

echo Maven found!
echo Java found!
echo.
echo Building and running the application...
echo.

REM Download dependencies and run
mvn clean compile exec:java

pause

