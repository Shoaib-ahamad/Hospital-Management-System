@echo off
REM Hospital Management System - Deployment Script for Windows
REM This script automates the Docker deployment process

setlocal enabledelayedexpansion

echo.
echo ==========================================
echo Hospital Management System - Docker Setup
echo ==========================================
echo.

REM Check if Docker is installed
echo Checking Docker installation...

where docker >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Docker is not installed
    echo Please install Docker Desktop from: https://www.docker.com/products/docker-desktop
    pause
    exit /b 1
)

where docker-compose >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Docker Compose is not installed
    echo Please install Docker Desktop (includes Docker Compose)
    pause
    exit /b 1
)

echo [OK] Docker and Docker Compose are installed
echo.

REM Check Docker daemon
echo Checking Docker daemon...
docker ps >nul 2>nul
if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Docker daemon is not running
    echo Please start Docker Desktop
    pause
    exit /b 1
)

echo [OK] Docker daemon is running
echo.

REM Build images
echo Building Docker images...
echo This may take several minutes on first run...
echo.

docker-compose build --no-cache

if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Failed to build images
    pause
    exit /b 1
)

echo [OK] Images built successfully
echo.

REM Start services
echo Starting services...
echo.

docker-compose up -d

if %ERRORLEVEL% NEQ 0 (
    echo [ERROR] Failed to start services
    pause
    exit /b 1
)

echo [OK] Services started
echo.

REM Wait for services
echo Waiting for services to be ready...
timeout /t 10 /nobreak

echo.
echo ==========================================
echo Services are running!
echo ==========================================
echo.

echo Frontend (Web UI):
echo   http://localhost:3000
echo.

echo Backend API:
echo   http://localhost:8080/api
echo.

echo Database:
echo   Host: localhost
echo   Port: 3306
echo   Username: root
echo   Password: root@123
echo   Database: hospital_db
echo.

echo Admin Credentials:
echo   Username: admin
echo   Password: admin123
echo.

echo ==========================================
echo Useful Docker Commands:
echo ==========================================
echo.

echo View logs:
echo   docker-compose logs -f
echo   docker-compose logs -f backend
echo   docker-compose logs -f frontend
echo.

echo Stop services:
echo   docker-compose down
echo.

echo Restart services:
echo   docker-compose restart
echo.

echo Access container:
echo   docker exec -it hospital-backend bash
echo   docker exec -it hospital-frontend sh
echo.

echo Remove all data (fresh start):
echo   docker-compose down -v
echo.

pause
