#!/bin/bash
# Hospital Management System - Deployment Script
# This script automates the Docker deployment process

set -e  # Exit on error

echo "=========================================="
echo "Hospital Management System - Docker Setup"
echo "=========================================="
echo ""

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Check if Docker is installed
check_docker() {
    echo "Checking Docker installation..."
    
    if ! command -v docker &> /dev/null; then
        echo -e "${RED}✗ Docker is not installed${NC}"
        echo "Please install Docker Desktop from: https://www.docker.com/products/docker-desktop"
        exit 1
    fi
    
    if ! command -v docker-compose &> /dev/null; then
        echo -e "${RED}✗ Docker Compose is not installed${NC}"
        echo "Please install Docker Desktop (includes Docker Compose)"
        exit 1
    fi
    
    echo -e "${GREEN}✓ Docker and Docker Compose are installed${NC}"
}

# Check Docker daemon
check_docker_daemon() {
    echo "Checking Docker daemon..."
    
    if ! docker ps &> /dev/null; then
        echo -e "${RED}✗ Docker daemon is not running${NC}"
        echo "Please start Docker Desktop"
        exit 1
    fi
    
    echo -e "${GREEN}✓ Docker daemon is running${NC}"
}

# Build images
build_images() {
    echo ""
    echo "Building Docker images..."
    echo "This may take several minutes on first run..."
    echo ""
    
    docker-compose build --no-cache
    
    echo -e "${GREEN}✓ Images built successfully${NC}"
}

# Start services
start_services() {
    echo ""
    echo "Starting services..."
    echo ""
    
    docker-compose up -d
    
    echo -e "${GREEN}✓ Services started${NC}"
}

# Wait for services to be ready
wait_for_services() {
    echo ""
    echo "Waiting for services to be ready..."
    
    # Wait for MySQL
    for i in {1..30}; do
        if docker exec hospital-mysql mysql -u root -proot@123 -e "SELECT 1" &> /dev/null; then
            echo -e "${GREEN}✓ MySQL is ready${NC}"
            break
        fi
        echo "Waiting for MySQL... ($i/30)"
        sleep 2
    done
    
    # Wait for Backend
    sleep 5
    echo -e "${GREEN}✓ Backend is ready${NC}"
    
    # Wait for Frontend
    sleep 2
    echo -e "${GREEN}✓ Frontend is ready${NC}"
}

# Show URLs
show_urls() {
    echo ""
    echo "=========================================="
    echo -e "${GREEN}Services are running!${NC}"
    echo "=========================================="
    echo ""
    echo "Frontend (Web UI):"
    echo -e "  ${YELLOW}http://localhost:3000${NC}"
    echo ""
    echo "Backend API:"
    echo -e "  ${YELLOW}http://localhost:8080/api${NC}"
    echo ""
    echo "Database:"
    echo -e "  Host: ${YELLOW}localhost${NC}"
    echo -e "  Port: ${YELLOW}3306${NC}"
    echo -e "  Username: ${YELLOW}root${NC}"
    echo -e "  Password: ${YELLOW}root@123${NC}"
    echo -e "  Database: ${YELLOW}hospital_db${NC}"
    echo ""
    echo "Admin Credentials:"
    echo -e "  Username: ${YELLOW}admin${NC}"
    echo -e "  Password: ${YELLOW}admin123${NC}"
    echo ""
}

# Show useful commands
show_commands() {
    echo "=========================================="
    echo "Useful Docker Commands:"
    echo "=========================================="
    echo ""
    echo "View logs:"
    echo "  docker-compose logs -f"
    echo "  docker-compose logs -f backend"
    echo "  docker-compose logs -f frontend"
    echo ""
    echo "Stop services:"
    echo "  docker-compose down"
    echo ""
    echo "Restart services:"
    echo "  docker-compose restart"
    echo ""
    echo "Access container:"
    echo "  docker exec -it hospital-backend bash"
    echo "  docker exec -it hospital-frontend sh"
    echo ""
    echo "Remove all data (fresh start):"
    echo "  docker-compose down -v"
    echo ""
}

# Main execution
main() {
    check_docker
    check_docker_daemon
    build_images
    start_services
    wait_for_services
    show_urls
    show_commands
}

# Run main function
main
