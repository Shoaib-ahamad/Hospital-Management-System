# Frontend & Backend Integration Guide

## Overview

This guide explains how to connect the frontend web application to the Java backend API.

## Architecture

```
Frontend (HTML/CSS/JS)
        ↓
    Fetch API
        ↓
Backend Java API (REST Endpoints)
        ↓
    Service Layer
        ↓
    DAO Layer
        ↓
    MySQL Database
```

## Current State

### Frontend
- ✅ Complete web application built with HTML/CSS/JS
- ✅ Mock data service for testing
- ✅ Ready to connect to backend API
- 📂 Location: `/frontend` folder

### Backend
- ✅ Java application with REST endpoints
- ✅ Service layer with business logic
- ✅ DAO layer for database operations
- 📂 Location: `/src/main/java/com/hospital` folder

## Step-by-Step Integration

### Step 1: Prepare Backend for Frontend Requests

The backend currently uses console-based I/O. To serve the frontend, you need to add REST API endpoints.

**Create REST API Layer** - Add new file: `src/main/java/com/hospital/api/PatientAPI.java`

```java
package com.hospital.api;

import com.hospital.model.Patient;
import com.hospital.service.PatientService;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PatientAPI {
    private PatientService patientService;
    private ObjectMapper objectMapper;

    public PatientAPI() {
        this.patientService = new PatientService();
        this.objectMapper = new ObjectMapper();
    }

    public Patient registerPatient(String json) throws Exception {
        Patient patient = objectMapper.readValue(json, Patient.class);
        int patientId = patientService.registerPatient(patient);
        patient.setPatientId(patientId);
        return patient;
    }

    public Patient loginPatient(String email) {
        return patientService.getPatientByEmail(email);
    }
}
```

**Note**: Full API layer implementation would require:
- Spring Boot for REST endpoints (recommended)
- Or Jersey/JAX-RS for REST implementation
- CORS configuration
- Request/Response JSON mapping

### Step 2: Update Frontend API Base URL

Edit `frontend/js/api.js`:

```javascript
// Before:
const API_BASE_URL = 'http://localhost:8080/api';

// Adjust based on your backend port:
const API_BASE_URL = 'http://localhost:8080/api';  // Port 8080
const API_BASE_URL = 'http://localhost:8081/api';  // Port 8081
const API_BASE_URL = 'http://localhost:9090/api';  // Port 9090
```

### Step 3: Switch to Real API Client

Edit `frontend/js/api.js`, near the bottom:

```javascript
// Change from:
const API = MockDataService;

// To:
const API = APIClient;
```

### Step 4: Start Both Applications

**Terminal 1 - Start Backend**:
```bash
cd d:\hospital_management
mvn -DskipTests compile exec:java -Dexec.mainClass=com.hospital.Main
```

**Terminal 2 - Start Frontend Server**:
```bash
cd d:\hospital_management\frontend
python -m http.server 5500
```

**Terminal 3 - Open Browser**:
```
http://localhost:5500
```

## Required Backend REST Endpoints

For full integration, implement these endpoints:

### Patient Endpoints

**POST** `/api/patients/register`
- Request: `{ name, email, phone, age, gender, address }`
- Response: `{ patientId, name, email, ... }`

**GET** `/api/patients/login?email={email}`
- Request: Query parameter `email`
- Response: `{ patientId, name, email, ... }`

**GET** `/api/patients/{patientId}`
- Request: Path parameter `patientId`
- Response: Patient object

**GET** `/api/patients`
- Request: None
- Response: Array of Patient objects

### Doctor Endpoints

**POST** `/api/doctors`
- Request: `{ name, specialization, email, phone, qualification, experience }`
- Response: `{ doctorId, name, specialization, ... }`

**GET** `/api/doctors`
- Request: None
- Response: Array of Doctor objects

**GET** `/api/doctors/{doctorId}`
- Request: Path parameter `doctorId`
- Response: Doctor object

**GET** `/api/doctors/specialization/{spec}`
- Request: Path parameter `spec`
- Response: Array of doctors with that specialization

### Appointment Endpoints

**POST** `/api/appointments/request`
- Request: `{ patientId, specialization, requestedDate, description }`
- Response: `{ requestId, patientId, specialization, status, ... }`

**GET** `/api/appointments/pending`
- Request: None
- Response: Array of pending requests

**GET** `/api/appointments/patient/{patientId}`
- Request: Path parameter `patientId`
- Response: Array of patient's appointments

**POST** `/api/appointments/fix`
- Request: `{ requestId, patientId, doctorId, appointmentDate }`
- Response: `{ appointmentId, patientId, doctorId, ... }`

**GET** `/api/appointments`
- Request: None
- Response: Array of all appointments

## CORS Configuration

Your backend needs CORS headers. Add to every response:

```
Access-Control-Allow-Origin: *
Access-Control-Allow-Methods: GET, POST, PUT, DELETE, OPTIONS
Access-Control-Allow-Headers: Content-Type, Authorization
Access-Control-Allow-Credentials: true
```

## Using Spring Boot (Recommended)

For easier REST API implementation, consider using Spring Boot:

```xml
<!-- Add to pom.xml -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
    <version>3.1.0</version>
</dependency>
```

Then create REST controller:

```java
@RestController
@RequestMapping("/api/patients")
@CrossOrigin(origins = "*")
public class PatientController {
    
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Patient patient) {
        // Implementation
    }
    
    @GetMapping("/login")
    public ResponseEntity<?> login(@RequestParam String email) {
        // Implementation
    }
}
```

## Testing the Integration

### 1. Test Mock Data First
- Keep `const API = MockDataService;` 
- Test all features locally
- Verify UI works correctly

### 2. Test Real Backend
- Start backend server
- Update `API_BASE_URL` in `api.js`
- Change to `const API = APIClient;`
- Reload frontend
- Test patient registration
- Test appointment request
- Check browser console for errors

### 3. Debug Tips
- **Open DevTools**: F12 in browser
- **Check Network Tab**: See API calls and responses
- **Check Console**: Look for JavaScript errors
- **Use Postman**: Test backend endpoints directly

### 4. Common Issues

**Issue**: API calls fail with 404
- **Fix**: Check endpoint URLs match exactly
- **Fix**: Verify backend is running on correct port

**Issue**: CORS errors
- **Fix**: Add CORS headers to backend responses
- **Fix**: Check `Access-Control-Allow-Origin` header

**Issue**: JSON parsing errors
- **Fix**: Ensure backend returns valid JSON
- **Fix**: Check data types match (int vs string)

**Issue**: Connection refused
- **Fix**: Verify backend server is running
- **Fix**: Check correct port in `API_BASE_URL`

## Development Workflow

### While Developing
```javascript
// Use mock data for fast iteration
const API = MockDataService;
```

### When Testing Integration
```javascript
// Switch to real API
const API = APIClient;
```

### For Production
```javascript
// Use real API with proper error handling
const API = APIClient;
```

## File Structure After Integration

```
hospital_management/
├── frontend/                      # Web UI
│   ├── index.html
│   ├── css/styles.css
│   ├── js/api.js                 # <-- Update API calls here
│   └── js/app.js
│
├── src/main/java/com/hospital/   # Backend
│   ├── Main.java
│   ├── api/                       # <-- Add REST controllers here
│   │   ├── PatientAPI.java
│   │   ├── DoctorAPI.java
│   │   └── AppointmentAPI.java
│   ├── dao/
│   ├── model/
│   ├── service/
│   └── util/
│
└── pom.xml
```

## Performance Optimization

### Frontend
- Use browser caching
- Minimize CSS/JS files
- Compress images
- Use CDN for external resources

### Backend
- Add database connection pooling
- Implement caching strategy
- Use pagination for large lists
- Add request validation

## Security Checklist

- [ ] Validate all inputs on backend
- [ ] Use HTTPS in production
- [ ] Implement proper authentication
- [ ] Add rate limiting
- [ ] Sanitize all outputs
- [ ] Use parameterized queries
- [ ] Never expose sensitive data
- [ ] Implement proper error handling

## Next Steps

1. **Review backend code** - Understand current implementation
2. **Add REST API layer** - Use Spring Boot or similar framework
3. **Implement endpoints** - Based on the list above
4. **Test with Postman** - Before connecting frontend
5. **Enable CORS** - Allow frontend to access backend
6. **Connect frontend** - Update API base URL and switch from mock
7. **Deploy** - Use Docker, AWS, or cloud platform

## Deployment Options

### Local Development
- Frontend: `http://localhost:5500`
- Backend: `http://localhost:8080`

### Docker Deployment
```dockerfile
# Frontend Dockerfile
FROM node:18
WORKDIR /app
COPY frontend/ .
RUN npx http-server -p 5500

# Backend Dockerfile (existing Java app)
FROM openjdk:11
WORKDIR /app
COPY . .
RUN mvn -DskipTests package
CMD ["java", "-jar", "target/hospital-management-system-1.0.0.jar"]
```

### Cloud Deployment
- **Netlify/Vercel**: Deploy frontend
- **Heroku/Railway**: Deploy backend
- **AWS/Azure**: Full stack deployment

## Support & Resources

- **Frontend Issues**: Check `frontend/README.md`
- **Backend Issues**: Check `PROJECT_COMPLETE_DETAILS.md`
- **Integration Help**: This file
- **General Questions**: GitHub Issues

---

**Status**: Ready for integration  
**Last Updated**: November 15, 2025  
**Version**: 1.0.0
