# Frontend-Backend Connection Guide

## ✅ Connection Complete!

All frontend JavaScript files have been updated to connect with the Java backend API.

---

## 📋 Updated Files

### Frontend JavaScript Files:
1. **`js/login.js`** - Now calls `POST /login`
2. **`js/dashboard.js`** - Now calls `GET /dashboard`
3. **`js/deposit.js`** - Now calls `POST /deposit`
4. **`js/withdraw.js`** - Now calls `POST /withdraw`
5. **`js/history.js`** - Now calls `GET /transactions`
6. **`js/api.js`** - New utility file (optional, not required)

### Backend:
- ✅ CORS headers already configured in `ResponseUtils.java`
- ✅ All endpoints ready to accept requests

---

## 🔌 API Endpoints Connected

| Frontend Page | Backend Endpoint | Method | Status |
|--------------|------------------|--------|--------|
| Login | `/login` | POST | ✅ Connected |
| Dashboard | `/dashboard` | GET | ✅ Connected |
| Deposit | `/deposit` | POST | ✅ Connected |
| Withdraw | `/withdraw` | POST | ✅ Connected |
| History | `/transactions` | GET | ✅ Connected |

---

## 🔐 Authentication Flow

1. **Login** → User submits credentials
2. **Backend validates** → Returns token in response
3. **Frontend stores token** → `localStorage.setItem('token', data.data.token)`
4. **All subsequent requests** → Include `Authorization: Bearer <token>` header
5. **Token validation** → Backend checks token on each request
6. **If token invalid** → Frontend redirects to login page

---

## 📦 Request/Response Formats

### Login Request:
```javascript
POST /login
Headers: { "Content-Type": "application/json" }
Body: { "username": "user@gmail.com", "password": "12345" }
```

### Login Response:
```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "token": "uuid-token-here",
    "username": "user@gmail.com",
    "accountId": "ACC001",
    "balance": 10000.0
  }
}
```

### Authenticated Requests:
```javascript
GET /dashboard
Headers: {
  "Content-Type": "application/json",
  "Authorization": "Bearer <token>"
}
```

---

## 🚀 How to Test

### Step 1: Start Backend
```powershell
cd backend
javac -d bin src/models/*.java src/services/*.java src/controllers/*.java src/utils/*.java src/Main.java
java -cp bin Main
```

Backend should show: `Server started on port 8080`

### Step 2: Start Frontend Server (Optional but Recommended)
```powershell
# In a new terminal
cd C:\Users\Hp\Desktop\on
python -m http.server 8000
```

### Step 3: Open Browser
Navigate to: `http://localhost:8000` (or open `index.html` directly)

### Step 4: Test Flow
1. **Login** with `user@gmail.com` / `12345`
2. **View Dashboard** - Should show balance from backend
3. **Make Deposit** - Should update balance via backend
4. **Make Withdrawal** - Should validate and update via backend
5. **View History** - Should show transactions from backend

---

## 🔧 Field Name Mapping

All field names match between frontend and backend:

| Frontend | Backend | Status |
|----------|---------|--------|
| `username` | `username` | ✅ Match |
| `password` | `password` | ✅ Match |
| `amount` | `amount` | ✅ Match |
| `balance` | `balance` | ✅ Match |
| `token` | `token` | ✅ Match |
| `transactions` | `transactions` | ✅ Match |

---

## 🛡️ CORS Configuration

Backend CORS headers are set in `ResponseUtils.java`:
- ✅ `Access-Control-Allow-Origin: *`
- ✅ `Access-Control-Allow-Methods: GET, POST, OPTIONS`
- ✅ `Access-Control-Allow-Headers: Content-Type, Authorization`

All endpoints handle OPTIONS preflight requests.

---

## ⚠️ Important Notes

1. **Backend must be running** before using frontend
2. **Token is stored in localStorage** - cleared on logout
3. **Automatic redirect** to login if token is missing/invalid
4. **Error handling** - Shows user-friendly error messages
5. **Fallback behavior** - Some pages fall back to localStorage if API fails

---

## 🐛 Troubleshooting

### "Connection error" message
- ✅ Check backend is running on port 8080
- ✅ Check browser console for CORS errors
- ✅ Verify backend server started successfully

### "Unauthorized" error
- ✅ Token may have expired (24 hours)
- ✅ Try logging in again
- ✅ Check token is stored: `localStorage.getItem('token')`

### Balance not updating
- ✅ Check backend response in browser Network tab
- ✅ Verify transaction was created successfully
- ✅ Refresh dashboard to reload from backend

---

## 📝 Code Changes Summary

### What Changed:
- ✅ Replaced `localStorage` operations with `fetch()` API calls
- ✅ Added token-based authentication
- ✅ Added error handling for API failures
- ✅ Added automatic redirect on authentication failure
- ✅ Maintained all UI functionality

### What Stayed the Same:
- ✅ All HTML files unchanged
- ✅ All CSS files unchanged
- ✅ UI design and layout unchanged
- ✅ User experience unchanged

---

## ✨ Testing Checklist

- [ ] Backend starts without errors
- [ ] Login works with correct credentials
- [ ] Login fails with wrong credentials
- [ ] Dashboard loads user data
- [ ] Deposit updates balance
- [ ] Withdraw validates balance
- [ ] Transaction history displays
- [ ] Logout clears session
- [ ] Unauthorized requests redirect to login

---

**Connection Status: ✅ COMPLETE**

All frontend pages are now connected to the backend API!

