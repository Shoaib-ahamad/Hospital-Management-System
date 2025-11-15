# 🚀 5-Minute Online Deployment Checklist

## BEST OPTION: Netlify + Railway (Free, Easy, 15 minutes)

### FRONTEND → Netlify

```
1. Go to: https://netlify.com
2. Sign up with GitHub
3. Click "New site from Git"
4. Select: Hospital-Management-System
5. Build command: (empty)
6. Publish directory: frontend
7. Deploy
8. Note your URL: https://xxxxx.netlify.app
```

✅ **Frontend live in 2 minutes!**

---

### BACKEND → Railway

```
1. Go to: https://railway.app
2. Sign up with GitHub
3. Create new project from GitHub
4. Add MySQL service
5. Add backend service
6. Set environment variables:
   - DATABASE_URL (from MySQL)
   - DATABASE_USER
   - DATABASE_PASSWORD
7. Build: mvn clean package -DskipTests
8. Start: java -jar target/hospital-management-system-1.0.0.jar
9. Deploy
10. Note your URL: https://xxxxx.railway.app
```

✅ **Backend live in 5 minutes!**

---

### CONNECT THEM

```
1. Edit: frontend/js/api.js
2. Change: const API_BASE_URL = 'https://xxxxx.railway.app/api'
3. Commit: git add . && git commit -m "Update API URL"
4. Push: git push origin main
5. Netlify auto-redeploys
```

✅ **Application is now ONLINE!**

---

## 🔗 Your Live URLs

```
Frontend:  https://[your-site].netlify.app
Backend:   https://[your-app].railway.app/api
Admin:     admin / admin123
Database:  (Private, behind backend)
```

---

## 🎯 What To Do Next

### Test It
1. Visit frontend URL
2. Register new patient
3. Request appointment
4. Login as admin (admin/admin123)
5. Assign doctors

### Share It
- Send frontend URL to team
- Backend API ready for mobile apps

### Scale It
- Both platforms have paid tiers
- Scale up when needed

---

**That's it! Your app is LIVE! 🎉**
