# Online Banking System - Backend (Java)

A pure Java backend implementation for the Online Banking System using in-memory storage. No frameworks, no database - just Java standard library.

## 📁 Project Structure

```
backend/
├── src/
│   ├── models/
│   │   ├── User.java              # User model
│   │   ├── Account.java          # Account model
│   │   ├── Transaction.java       # Transaction model
│   │   └── Session.java          # Session model
│   ├── services/
│   │   ├── AuthService.java       # Authentication service
│   │   ├── SessionService.java    # Session management
│   │   ├── AccountService.java    # Account operations
│   │   └── TransactionService.java # Transaction management
│   ├── controllers/
│   │   ├── LoginController.java   # POST /login
│   │   ├── DashboardController.java # GET /dashboard
│   │   ├── DepositController.java  # POST /deposit
│   │   ├── WithdrawController.java # POST /withdraw
│   │   ├── TransferController.java # POST /transfer
│   │   └── TransactionHistoryController.java # GET /transactions
│   ├── utils/
│   │   ├── JSONUtils.java         # JSON utilities
│   │   └── ResponseUtils.java     # HTTP response utilities
│   └── Main.java                  # Server entry point
└── README.md
```

## 🚀 How to Compile and Run

### Prerequisites
- Java JDK 8 or higher
- No external dependencies required

### Compilation

1. **Navigate to the backend directory:**
   ```bash
   cd backend
   ```

2. **Compile all Java files:**
   ```bash
   javac -d bin src/**/*.java src/Main.java
   ```
   
   Or on Windows PowerShell:
   ```powershell
   javac -d bin src/models/*.java src/services/*.java src/controllers/*.java src/utils/*.java src/Main.java
   ```

3. **Run the server:**
   ```bash
   java -cp bin Main
   ```

   Or directly:
   ```bash
   java -cp bin src/Main.java
   ```

### Alternative: Compile and Run in One Step

**Windows:**
```powershell
cd backend
javac -d bin src/models/*.java src/services/*.java src/controllers/*.java src/utils/*.java src/Main.java
java -cp bin Main
```

**Linux/Mac:**
```bash
cd backend
javac -d bin src/**/*.java src/Main.java
java -cp bin Main
```

## 🌐 API Endpoints

The server runs on `http://localhost:8080`

### 1. POST /login
Authenticate user and create session.

**Request Body:**
```json
{
  "username": "user@gmail.com",
  "password": "12345"
}
```

**Response:**
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

### 2. GET /dashboard
Get user dashboard data (requires authentication).

**Headers:**
```
Authorization: Bearer <token>
```

**Or Query Parameter:**
```
?token=<token>
```

**Response:**
```json
{
  "success": true,
  "message": "Dashboard data retrieved successfully",
  "data": {
    "username": "user@gmail.com",
    "accountId": "ACC001",
    "balance": 10000.0,
    "recentTransactions": [...]
  }
}
```

### 3. POST /deposit
Deposit money to account (requires authentication).

**Request Body:**
```json
{
  "amount": "5000"
}
```

**Response:**
```json
{
  "success": true,
  "message": "Deposit successful",
  "data": {
    "amount": 5000.0,
    "newBalance": 15000.0,
    "message": "Successfully deposited Rs 5000.00. New balance: Rs 15000.00"
  }
}
```

### 4. POST /withdraw
Withdraw money from account (requires authentication).

**Request Body:**
```json
{
  "amount": "2000"
}
```

**Response:**
```json
{
  "success": true,
  "message": "Withdrawal successful",
  "data": {
    "amount": 2000.0,
    "newBalance": 8000.0,
    "message": "Successfully withdrew Rs 2000.00. Remaining balance: Rs 8000.00"
  }
}
```

### 5. POST /transfer
Transfer money between accounts (requires authentication).

**Request Body:**
```json
{
  "toAccountId": "ACC002",
  "amount": "1000"
}
```

**Response:**
```json
{
  "success": true,
  "message": "Transfer successful",
  "data": {
    "amount": 1000.0,
    "toAccountId": "ACC002",
    "newBalance": 7000.0,
    "message": "Successfully transferred Rs 1000.00 to account ACC002. Remaining balance: Rs 7000.00"
  }
}
```

### 6. GET /transactions
Get transaction history (requires authentication).

**Response:**
```json
{
  "success": true,
  "message": "Transaction history retrieved successfully",
  "data": {
    "accountId": "ACC001",
    "transactions": [
      {
        "transactionId": "...",
        "type": "Deposit",
        "amount": 5000.0,
        "timestamp": "2024-01-01T12:00:00Z",
        "description": "Deposited Rs 5000.00"
      }
    ],
    "count": 1
  }
}
```

## 🔐 Default Credentials

- **Username:** `user@gmail.com`
- **Password:** `12345`
- **Initial Balance:** Rs 10,000

## 🔧 Technical Details

### In-Memory Storage
- **Users:** Stored in `HashMap<String, User>`
- **Accounts:** Stored in `HashMap<String, Account>`
- **Transactions:** Stored in `ArrayList<Transaction>`
- **Sessions:** Stored in `ConcurrentHashMap<String, Session>`

### Session Management
- Sessions use UUID tokens
- Sessions expire after 24 hours
- Sessions are automatically extended on access
- Token can be passed via:
  - `Authorization: Bearer <token>` header
  - `?token=<token>` query parameter

### JSON Handling
- Custom JSON builder (no external libraries)
- Simple JSON parser for request bodies
- Proper escaping of special characters

### CORS Support
- All endpoints support CORS
- Preflight OPTIONS requests are handled
- Headers: `Access-Control-Allow-Origin: *`

## 📝 Error Responses

All errors follow this format:

```json
{
  "success": false,
  "message": "Error message here"
}
```

**Common HTTP Status Codes:**
- `200` - Success
- `400` - Bad Request (validation errors)
- `401` - Unauthorized (invalid/missing token)
- `404` - Not Found (account/user not found)
- `405` - Method Not Allowed
- `500` - Internal Server Error

## 🧪 Testing with cURL

### Login
```bash
curl -X POST http://localhost:8080/login \
  -H "Content-Type: application/json" \
  -d '{"username":"user@gmail.com","password":"12345"}'
```

### Dashboard (replace TOKEN with actual token)
```bash
curl -X GET http://localhost:8080/dashboard \
  -H "Authorization: Bearer TOKEN"
```

### Deposit
```bash
curl -X POST http://localhost:8080/deposit \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer TOKEN" \
  -d '{"amount":"5000"}'
```

### Withdraw
```bash
curl -X POST http://localhost:8080/withdraw \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer TOKEN" \
  -d '{"amount":"2000"}'
```

### Get Transactions
```bash
curl -X GET http://localhost:8080/transactions \
  -H "Authorization: Bearer TOKEN"
```

## 🔄 Data Persistence

**Note:** All data is stored in memory. When the server stops, all data is lost. This is intentional for a simple demonstration backend.

## 🛠️ Customization

### Change Default User
Edit `src/services/AuthService.java`:
```java
private void initializeDefaultUser() {
    User defaultUser = new User("your-email@example.com", "your-password", "ACC001");
    users.put("your-email@example.com", defaultUser);
}
```

### Change Initial Balance
Edit `src/services/AccountService.java`:
```java
private void initializeDefaultAccount() {
    Account account = new Account("ACC001", "user@gmail.com", 50000.0); // Change amount
    // ...
}
```

### Change Server Port
Edit `src/Main.java`:
```java
private static final int PORT = 3000; // Change port
```

## 📦 Dependencies

**None!** This project uses only Java Standard Library:
- `com.sun.net.httpserver.*` - HTTP server
- `java.util.*` - Collections and utilities
- `java.time.*` - Date/time handling
- `java.util.UUID` - Session tokens

## ⚠️ Important Notes

- This is a **demonstration backend** - not suitable for production
- No database persistence - data is lost on server restart
- Simple JSON parsing - may not handle all edge cases
- No password hashing - passwords stored in plain text
- Single-threaded by default (can be configured)

## 🐛 Troubleshooting

**Port already in use:**
- Change the PORT constant in `Main.java`
- Or stop the process using port 8080

**Compilation errors:**
- Ensure you're using JDK 8 or higher
- Check that all files are in the correct package directories

**Connection refused:**
- Make sure the server is running
- Check firewall settings
- Verify the port number

---

**Enjoy your banking backend! 🏦**

