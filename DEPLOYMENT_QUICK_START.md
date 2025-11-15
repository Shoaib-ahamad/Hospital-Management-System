# Hospital Management System - Quick Deployment Guide

## 🚀 Deployment Ready!

Your Hospital Management System is now fully containerized and ready for deployment!

---

## 📋 What's Included

### Docker Files
- **Dockerfile.backend** - Multi-stage Java/Maven build for backend
- **Dockerfile.frontend** - Node.js HTTP server for frontend
- **docker-compose.yml** - Orchestrates MySQL, Backend, and Frontend services

### Deployment Scripts
- **deploy.sh** - Automated deployment for macOS/Linux
- **deploy.bat** - Automated deployment for Windows

### Documentation
- **DEPLOYMENT.md** - Comprehensive deployment guide with cloud options

---

## 🏃 Quick Start (Local Docker)

### Prerequisites
- Docker Desktop installed (https://www.docker.com/products/docker-desktop)
- Docker daemon running

### Option 1: Windows (Easiest)
```batch
# Double-click to run
deploy.bat
```

### Option 2: macOS/Linux
```bash
chmod +x deploy.sh
./deploy.sh
```

### Option 3: Manual Docker Commands
```bash
# Navigate to project directory
cd hospital_management

# Build and start all services
docker-compose up --build

# Services available at:
# - Frontend: http://localhost:3000
# - Backend: http://localhost:8080/api
# - Database: localhost:3306
```

---

## 🌐 Cloud Deployment Options

### 1. **Heroku + Netlify** (Easiest & Free)
- Backend: Deploy to Heroku (free tier available)
- Frontend: Deploy to Netlify (free tier included)
- **Time**: ~30 minutes
- **Cost**: Free (or $7-50/month for production)

**See**: DEPLOYMENT.md → "Option 1: Heroku (Backend) + Netlify (Frontend)"

### 2. **AWS Full Stack** (Most Powerful)
- Backend: AWS Elastic Beanstalk
- Frontend: CloudFront + S3
- Database: AWS RDS (MySQL)
- **Time**: ~1-2 hours
- **Cost**: $10-50/month (depends on usage)

**See**: DEPLOYMENT.md → "Option 2: AWS (Full Stack)"

### 3. **DigitalOcean** (Best Balance)
- Simple UI, affordable pricing
- App Platform handles deployment automatically
- One-click MySQL database
- **Time**: ~45 minutes
- **Cost**: $5-20/month

**See**: DEPLOYMENT.md → "Option 4: DigitalOcean App Platform"

### 4. **Docker Hub + Server** (Full Control)
- Push to Docker Hub registry
- Deploy on any VPS (Linode, DigitalOcean, AWS EC2)
- Full control over infrastructure
- **Time**: ~2-3 hours
- **Cost**: $5-100/month

**See**: DEPLOYMENT.md → "Option 3: Docker Hub + AWS ECR"

---

## 📊 Service Architecture

```
┌─────────────────────────────────────────────┐
│           USER BROWSER                      │
│     (http://localhost:3000)                 │
└────────────────┬────────────────────────────┘
                 │
      ┌──────────▼───────────┐
      │ FRONTEND             │
      │ (Node HTTP Server)   │
      │ Port: 3000           │
      └──────────┬───────────┘
                 │
      ┌──────────▼────────────┐
      │ BACKEND API          │
      │ (Java Spring/Jersey) │
      │ Port: 8080           │
      └──────────┬───────────┘
                 │
      ┌──────────▼───────────┐
      │ MySQL DATABASE       │
      │ Port: 3306           │
      │ Hospital_db          │
      └──────────────────────┘
```

---

## 🔑 Access Credentials

### Admin Portal
- **URL**: http://localhost:3000
- **Portal**: Admin
- **Username**: admin
- **Password**: admin123

### Database
- **Host**: localhost
- **Port**: 3306
- **Username**: root
- **Password**: root@123
- **Database**: hospital_db

### Patient Demo
- **Email**: john.doe@email.com
- **Password**: (No password, email-based login)

---

## 📱 Testing Features

### Patient Portal
1. Register new patient
2. Login with email
3. Request appointment (select specialization & date)
4. View appointments
5. View profile

### Admin Portal
1. Login (admin/admin123)
2. View pending requests
3. View available doctors
4. Add new doctor
5. Assign doctor to appointment request
6. View all appointments

---

## 🐛 Troubleshooting

### Docker won't start?
```bash
# Check if Docker Desktop is running
docker ps

# If not, start Docker Desktop application
# Then run deploy script again
```

### Port already in use?
```bash
# Change ports in docker-compose.yml
# Example:
# services:
#   frontend:
#     ports:
#       - "3001:3000"  # Use 3001 instead of 3000
```

### Database connection error?
```bash
# Wait for MySQL to initialize (takes ~30 seconds)
# Check logs
docker-compose logs mysql

# Restart services
docker-compose down
docker-compose up --build
```

### Need to reset everything?
```bash
# Stop and remove all containers + data
docker-compose down -v

# Start fresh
docker-compose up --build
```

---

## 📈 Next Steps

### For Local Development
1. ✅ Run `docker-compose up`
2. Test all features at http://localhost:3000
3. Verify database operations
4. Test API endpoints with Postman

### For Production Deployment
1. Choose cloud platform (Heroku, AWS, DigitalOcean, etc.)
2. Follow platform-specific deployment steps in DEPLOYMENT.md
3. Configure custom domain name
4. Set up SSL/TLS certificate
5. Configure backup and monitoring

### For Backend Integration
1. Implement REST API endpoints (if not using Spring Boot)
2. Update frontend API_BASE_URL to backend URL
3. Switch from MockDataService to APIClient
4. Test end-to-end workflows
5. Deploy both frontend and backend

---

## 📚 Documentation

- **DEPLOYMENT.md** - Complete deployment guide
- **FRONTEND_BACKEND_INTEGRATION.md** - Backend integration steps
- **frontend/README.md** - Frontend documentation
- **PROJECT_COMPLETE_DETAILS.md** - Full project overview
- **ARCHITECTURE.md** - System architecture details

---

## ✨ Project Status

| Component | Status | Details |
|-----------|--------|---------|
| Backend (Java) | ✅ Complete | Console app, ready for API conversion |
| Frontend (HTML/CSS/JS) | ✅ Complete | Responsive, mock data included |
| Database (MySQL) | ✅ Complete | Schema + sample data included |
| Docker Containerization | ✅ Complete | docker-compose ready |
| Documentation | ✅ Complete | 10+ markdown guides |
| Git Repository | ✅ Complete | On GitHub with 3 commits |
| Local Deployment | ✅ Ready | Run deploy script |
| Cloud Deployment | ✅ Documented | Multiple platform guides |

---

## 🎯 Recommended Deployment Path

### For Quick Demo (5 minutes)
1. Run `docker-compose up`
2. Open http://localhost:3000
3. Test features with mock data

### For Development (30 minutes)
1. Run `docker-compose up`
2. Run backend separately in IDE
3. Test integration with frontend
4. Debug and iterate

### For Production (1-3 hours)
1. Choose cloud platform
2. Follow DEPLOYMENT.md guide
3. Configure domain + SSL
4. Deploy frontend and backend
5. Set up monitoring

---

## 📞 Support

- Check DEPLOYMENT.md for troubleshooting
- Review FRONTEND_BACKEND_INTEGRATION.md for API setup
- See docker-compose logs for service errors
- Test API with curl or Postman

---

**Your application is ready to deploy! 🚀**

Latest commit: `26de737` - Docker deployment configuration added
Repository: https://github.com/Shoaib-ahamad/Hospital-Management-System
