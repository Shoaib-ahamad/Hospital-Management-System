# Hospital Management System - Deployment Guide

## Table of Contents
1. [Local Docker Deployment](#local-docker-deployment)
2. [Cloud Deployment Options](#cloud-deployment-options)
3. [Environment Configuration](#environment-configuration)
4. [Monitoring & Troubleshooting](#monitoring--troubleshooting)

---

## Local Docker Deployment

### Prerequisites
- **Docker Desktop** installed (https://www.docker.com/products/docker-desktop)
- **Docker Compose** (included with Docker Desktop)
- **Git** for version control

### Quick Start (One Command)

```bash
# Navigate to project directory
cd hospital_management

# Start all services (MySQL, Backend, Frontend)
docker-compose up --build

# Services will be available at:
# - Frontend: http://localhost:3000
# - Backend API: http://localhost:8080
# - MySQL: localhost:3306
```

### Manual Steps

```bash
# 1. Build images
docker-compose build

# 2. Start services
docker-compose up

# 3. View logs
docker-compose logs -f

# 4. Stop services
docker-compose down

# 5. Remove all data (fresh start)
docker-compose down -v
```

### Verify Deployment

```bash
# Check running containers
docker ps

# Expected output:
# hospital-frontend
# hospital-backend
# hospital-mysql

# View logs for specific service
docker logs hospital-backend
docker logs hospital-frontend
docker logs hospital-mysql

# Access container shell (debugging)
docker exec -it hospital-backend bash
docker exec -it hospital-frontend sh
docker exec -it hospital-mysql mysql -u root -p
```

### Accessing Services

**Frontend**: http://localhost:3000
- Patient Portal: Register, login, request appointments
- Admin Portal: Manage doctors, review appointments
- Demo Credentials: admin/admin123

**Backend API**: http://localhost:8080/api
- Available endpoints documented in FRONTEND_BACKEND_INTEGRATION.md
- Test with Postman or curl

**Database**: localhost:3306
- Username: root
- Password: root@123
- Database: hospital_db

### Data Persistence

MySQL data is stored in a Docker volume:
```bash
# List volumes
docker volume ls

# Inspect volume
docker volume inspect hospital_mysql_data

# Remove volume (deletes all data)
docker volume rm hospital_mysql_data
```

---

## Cloud Deployment Options

### Option 1: Heroku (Backend) + Netlify (Frontend)

#### Step 1: Prepare Heroku Account
```bash
# Install Heroku CLI
# https://devcenter.heroku.com/articles/heroku-cli

# Login to Heroku
heroku login

# Create new app
heroku create hospital-management-app

# Add MySQL database addon
heroku addons:create cleardb:ignite
```

#### Step 2: Deploy Backend to Heroku

Create `Procfile` in project root:
```
web: java -cp target/classes com.hospital.Main
```

```bash
# Deploy
git push heroku main

# View logs
heroku logs --tail

# Set environment variables
heroku config:set DATABASE_URL=<your-cleardb-url>
heroku config:set DATABASE_USER=<username>
heroku config:set DATABASE_PASSWORD=<password>
```

**Backend URL**: `https://hospital-management-app.herokuapp.com`

#### Step 3: Deploy Frontend to Netlify

```bash
# Install Netlify CLI
npm install -g netlify-cli

# Login to Netlify
netlify login

# Create production build
cd frontend
npm run build  # or use build-essential

# Deploy
netlify deploy --prod

# Or use GitHub integration:
# 1. Push to GitHub
# 2. Connect GitHub repo to Netlify
# 3. Set build command: cd frontend && npm build
# 4. Set publish directory: frontend/dist (or frontend/)
```

**Frontend URL**: `https://hospital-management.netlify.app`

### Option 2: AWS (Full Stack)

#### Backend: AWS Elastic Beanstalk

```bash
# Install AWS EB CLI
pip install awsebcli

# Initialize EB app
eb init -p java-11 hospital-management

# Create environment
eb create production

# Deploy
eb deploy

# View logs
eb logs
```

#### Database: AWS RDS (MySQL)

1. Create RDS MySQL instance
2. Get connection string
3. Update `db.properties` or environment variables
4. Run database schema initialization

#### Frontend: AWS CloudFront + S3

1. Build frontend: `npm build`
2. Upload to S3 bucket
3. Configure CloudFront distribution
4. Set up Route 53 DNS

### Option 3: Docker Hub + AWS ECR

#### Push to Docker Hub

```bash
# Login to Docker Hub
docker login

# Tag images
docker tag hospital-backend:latest yourusername/hospital-backend:latest
docker tag hospital-frontend:latest yourusername/hospital-frontend:latest

# Push images
docker push yourusername/hospital-backend:latest
docker push yourusername/hospital-frontend:latest
```

#### Deploy with Docker Swarm or Kubernetes

```bash
# Using docker swarm
docker swarm init
docker stack deploy -c docker-compose.yml hospital

# Using kubectl
kubectl apply -f kubernetes-deployment.yml
```

### Option 4: DigitalOcean App Platform

```bash
# 1. Create DigitalOcean account
# 2. Connect GitHub repository
# 3. Create App:
#    - Source: GitHub (Hospital-Management-System)
#    - Components:
#      - Backend: Docker (Dockerfile.backend)
#      - Frontend: Docker (Dockerfile.frontend)
#      - Database: MySQL database component
# 4. Set environment variables
# 5. Deploy
```

---

## Environment Configuration

### Backend Environment Variables

| Variable | Example | Purpose |
|----------|---------|---------|
| `DATABASE_URL` | `jdbc:mysql://localhost:3306/hospital_db` | Database connection URL |
| `DATABASE_USER` | `root` | Database username |
| `DATABASE_PASSWORD` | `root@123` | Database password |
| `API_PORT` | `8080` | Backend API port |
| `LOG_LEVEL` | `INFO` | Logging level |

### Frontend Environment Variables

| Variable | Example | Purpose |
|----------|---------|---------|
| `API_BASE_URL` | `http://localhost:8080/api` | Backend API base URL |
| `FRONTEND_PORT` | `3000` | Frontend server port |
| `DEBUG` | `false` | Debug mode |

### Docker Environment File (.env)

Create `.env` file in project root:

```env
# MySQL Configuration
MYSQL_ROOT_PASSWORD=root@123
MYSQL_DATABASE=hospital_db
MYSQL_USER=hospital_user
MYSQL_PASSWORD=hospital_pass123

# Backend Configuration
DATABASE_URL=jdbc:mysql://mysql:3306/hospital_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
DATABASE_USER=root
DATABASE_PASSWORD=root@123
BACKEND_PORT=8080

# Frontend Configuration
API_BASE_URL=http://localhost:8080/api
FRONTEND_PORT=3000
```

Use in docker-compose:
```yaml
services:
  backend:
    environment:
      - DATABASE_URL=${DATABASE_URL}
      - DATABASE_USER=${DATABASE_USER}
      - DATABASE_PASSWORD=${DATABASE_PASSWORD}
```

---

## Monitoring & Troubleshooting

### Health Checks

All containers include health checks:

```bash
# View health status
docker ps --format "table {{.Names}}\t{{.Status}}"

# Expected:
# hospital-frontend    Up 2 minutes (healthy)
# hospital-backend     Up 2 minutes (healthy)
# hospital-mysql       Up 2 minutes (healthy)
```

### Common Issues & Solutions

#### Issue: Backend cannot connect to MySQL
```
Error: Access denied for user 'root'@'mysql' (using password: YES)
```

**Solution:**
```bash
# Verify MySQL is running
docker logs hospital-mysql

# Check database credentials in docker-compose.yml
# Verify DATABASE_URL environment variable

# Restart MySQL service
docker-compose restart mysql
```

#### Issue: Frontend shows blank page
```
Error: Failed to fetch from API
```

**Solution:**
```bash
# Check backend is running
curl http://localhost:8080/api/patients

# Update API_BASE_URL in frontend
# Edit: frontend/js/api.js
# Change: const API_BASE_URL = 'http://backend:8080/api'

# Rebuild frontend container
docker-compose build frontend
docker-compose restart frontend
```

#### Issue: Port already in use
```
Error: listen EADDRINUSE :::3000
```

**Solution:**
```bash
# Find process using port
lsof -i :3000  # macOS/Linux
netstat -ano | findstr :3000  # Windows

# Kill process
kill -9 <PID>  # macOS/Linux
taskkill /PID <PID> /F  # Windows

# Or change port in docker-compose.yml
# Change: ports: - "3001:3000"
```

#### Issue: Docker daemon not running
```
Error: Cannot connect to Docker daemon
```

**Solution:**
- Start Docker Desktop
- Or: `sudo systemctl start docker` (Linux)

#### Issue: Out of disk space
```
Error: no space left on device
```

**Solution:**
```bash
# Clean up Docker resources
docker system prune -a

# Remove unused volumes
docker volume prune

# Remove unused networks
docker network prune
```

### Debugging

#### View Real-time Logs

```bash
# All services
docker-compose logs -f

# Specific service
docker-compose logs -f backend
docker-compose logs -f frontend
docker-compose logs -f mysql

# Last 100 lines
docker-compose logs --tail=100 backend
```

#### Access Container Terminal

```bash
# Backend (Java)
docker exec -it hospital-backend bash

# Frontend (Node)
docker exec -it hospital-frontend sh

# MySQL
docker exec -it hospital-mysql mysql -u root -p
```

#### Test API Endpoints

```bash
# Get all patients
curl http://localhost:8080/api/patients

# Register patient
curl -X POST http://localhost:8080/api/patients/register \
  -H "Content-Type: application/json" \
  -d '{"name":"John","email":"john@test.com","phone":"123"}'

# Get doctors
curl http://localhost:8080/api/doctors
```

#### Database Connection Test

```bash
# From local machine
mysql -h localhost -u root -p

# From backend container
docker exec hospital-backend mysql -h mysql -u root -p

# Password: root@123
```

---

## Performance Optimization

### Backend Optimization

```dockerfile
# In Dockerfile.backend
ENV JAVA_OPTS="-Xmx512m -Xms256m"
CMD ["java", "$JAVA_OPTS", "-jar", "hospital-management-system-1.0.0.jar"]
```

### Frontend Optimization

```dockerfile
# In Dockerfile.frontend
# Use minified files
RUN npm run build

# Serve with compression
CMD ["http-server", "-p", "3000", "-g"]
```

### Database Optimization

```yaml
# In docker-compose.yml
mysql:
  command: 
    - --innodb-buffer-pool-size=256M
    - --max-connections=100
```

---

## Security Considerations

### Secrets Management

```bash
# Never commit secrets to Git
echo ".env" >> .gitignore

# Use Docker secrets for production
docker secret create db_password -

# Or environment variables on cloud platform
heroku config:set DATABASE_PASSWORD=<secure-password>
```

### Network Security

```yaml
# Restrict backend access
backend:
  expose:  # Only expose to internal network
    - 8080
  ports:   # Remove public port in production
    - "8080:8080"
```

### SSL/TLS

```bash
# Use reverse proxy (Nginx)
# Configure SSL certificate (Let's Encrypt)
# Update API_BASE_URL to https://
```

---

## Backup & Recovery

### Backup Database

```bash
# Export database dump
docker exec hospital-mysql mysqldump -u root -p hospital_db > backup.sql

# Or use volume backup
docker run --volumes-from hospital-mysql -v $(pwd):/backup \
  alpine tar czf /backup/mysql-backup.tar.gz /var/lib/mysql
```

### Restore Database

```bash
# Restore from dump
docker exec -i hospital-mysql mysql -u root -p < backup.sql

# Or restore volume
docker run --volumes-from hospital-mysql -v $(pwd):/backup \
  alpine tar xzf /backup/mysql-backup.tar.gz -C /
```

---

## Next Steps

1. **Local Testing**: `docker-compose up` and test all features
2. **Choose Cloud Platform**: Based on budget and requirements
3. **Deploy Backend**: To cloud provider
4. **Deploy Frontend**: To CDN or app platform
5. **Setup Monitoring**: CloudWatch, Datadog, or New Relic
6. **Configure CI/CD**: GitHub Actions for automated deployment
7. **Scale**: Use load balancing if needed

---

**Last Updated**: November 15, 2025  
**Version**: 1.0.0
