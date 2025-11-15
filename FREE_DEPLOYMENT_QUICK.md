# ✅ FREE Deployment - 15 Minute Checklist (No Credit Card!)

## 🆓 BEST FREE OPTION: Netlify + Railway

---

## **FRONTEND → NETLIFY (FREE FOREVER) - 5 minutes**

### ✅ Step 1: Create Account
```
1. Go: https://netlify.com
2. Click: "Sign up"
3. Choose: "GitHub"
4. Authorize
```

### ✅ Step 2: Deploy
```
1. Click: "New site from Git"
2. Select: Hospital-Management-System
3. Build command: (leave empty)
4. Publish directory: frontend
5. Deploy!
```

### ✅ Step 3: Get Your Frontend URL
```
Looks like: https://hospital-management-abc123.netlify.app
```

---

## **BACKEND → RAILWAY (FREE TRIAL) - 8 minutes**

### ✅ Step 1: Create Account
```
1. Go: https://railway.app
2. Click: "Start New Project"
3. Sign up with GitHub
4. Authorize
```

### ✅ Step 2: Add MySQL (FREE)
```
1. Click: "+ Add Service"
2. Select: "MySQL"
3. Copy credentials
```

### ✅ Step 3: Add Backend
```
1. Click: "+ Add Service" → "GitHub Repo"
2. Select: Hospital-Management-System
3. Branch: main
```

### ✅ Step 4: Set Variables
```
Go to Backend Service → Variables

Add:
DATABASE_URL=mysql://root:password@host:3306/hospital_db
DATABASE_USER=root
DATABASE_PASSWORD=your_password
PORT=8080
```

### ✅ Step 5: Build & Start
```
Build: mvn clean package -DskipTests
Start: java -jar target/hospital-management-system-1.0.0.jar
```

### ✅ Step 6: Deploy
```
Wait 5-10 minutes
Get URL: https://hospital-backend-prod.railway.app
```

---

## **CONNECT THEM - 2 minutes**

### ✅ Update Frontend
```
Edit: frontend/js/api.js

Change:
const API_BASE_URL = 'http://localhost:8080/api';

To:
const API_BASE_URL = 'https://hospital-backend-prod.railway.app/api';
```

### ✅ Push to GitHub
```bash
git add frontend/js/api.js
git commit -m "Update backend URL"
git push origin main
```

### ✅ Automatic Redeploy
```
Netlify detects changes
Auto-redeploys
Wait 1-2 minutes
```

---

## 🎉 **DONE! Your App is LIVE & FREE!**

```
Frontend: https://hospital-management-abc123.netlify.app
Backend:  https://hospital-backend-prod.railway.app
Cost:     $0/month ✅
```

---

## 🧪 Quick Test

1. Visit frontend URL
2. Register as patient
3. Login
4. Request appointment
5. Admin login (admin/admin123)
6. Assign doctor
7. ✅ Everything works!

---

**Total Time: 15 minutes**  
**Total Cost: $0/month**  
**Credit Card: Not needed!**

**Your app is LIVE! 🚀**
