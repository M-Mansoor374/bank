# Online Banking System (Frontend Only)

A complete, fully functional online banking simulation built with HTML, CSS, and vanilla JavaScript. No backend, no frameworks, and no external libraries required.

## 📁 Project Structure

```
on/
├── css/
│   └── styles.css          # Shared stylesheet with modern UI design
├── js/
│   ├── init.js             # Initialization script for localStorage setup
│   ├── login.js            # Login page logic
│   ├── dashboard.js        # Dashboard page logic
│   ├── deposit.js          # Deposit transaction logic
│   ├── withdraw.js         # Withdrawal transaction logic
│   └── history.js          # Transaction history display logic
├── index.html              # Login page
├── dashboard.html          # Main dashboard
├── deposit.html            # Deposit money page
├── withdraw.html           # Withdraw money page
├── history.html            # Transaction history page
└── README.md               # This file
```

## 🚀 How to Run

1. **Open the project folder** in your file explorer
2. **Double-click on `index.html`** to open it in your default web browser
   - OR right-click `index.html` → Open with → Choose your preferred browser
3. **No server required!** The application runs entirely in the browser using localStorage

## 🔐 Default Login Credentials

- **Username/Email:** `user@gmail.com`
- **Password:** `12345`

## ✨ Features

### 1. Login Page (`index.html`)
- Email/username and password authentication
- Form validation (no empty fields)
- Error message display for invalid credentials
- Automatic redirect to dashboard on successful login

### 2. Dashboard (`dashboard.html`)
- Welcome message with username
- Current account balance display
- Navigation menu with links to:
  - Deposit money
  - Withdraw money
  - Transaction history
  - Logout

### 3. Deposit Page (`deposit.html`)
- Input field for deposit amount
- Validation for positive numeric values
- Real-time balance update
- Transaction recording with timestamp
- Success message and automatic redirect

### 4. Withdraw Page (`withdraw.html`)
- Input field for withdrawal amount
- Validation for:
  - Positive numeric values
  - Sufficient balance check
- Real-time balance update
- Transaction recording with timestamp
- Success message and automatic redirect

### 5. Transaction History (`history.html`)
- Complete transaction list in table format
- Displays:
  - Transaction type (Deposit/Withdraw)
  - Amount (with +/- indicators)
  - Description
  - Date and time
- Sorted by newest first
- Empty state message when no transactions exist

### 6. Logout
- Clears session status
- Redirects to login page
- Confirmation dialog before logout

## 💾 Data Storage

All data is stored in the browser's `localStorage`:

- **`userCredentials`**: User login credentials (JSON)
- **`balance`**: Current account balance (string)
- **`transactions`**: Array of transaction objects (JSON)
- **`loggedIn`**: Session status (string: "true" or "false")
- **`currentUser`**: Currently logged-in user (string)

### Transaction Object Structure
```javascript
{
    type: "Deposit" | "Withdraw",
    amount: number,
    timestamp: ISO string,
    description: string
}
```

## 🎨 Design Features

- Modern gradient background
- Clean, card-based UI design
- Responsive layout (works on desktop, tablet, and mobile)
- Smooth animations and transitions
- Color-coded transaction types (green for deposits, red for withdrawals)
- Professional banking interface

## 🔧 Technical Details

- **Pure HTML5** for structure
- **CSS3** with Flexbox and Grid for layout
- **Vanilla JavaScript (ES5)** for functionality
- **localStorage API** for data persistence
- **No dependencies** - runs entirely in the browser

## 📝 Notes

- Data persists in localStorage until browser data is cleared
- Each transaction includes a timestamp
- Balance is updated in real-time
- Session is maintained across page navigations
- All pages check for authentication before allowing access

## 🧪 Testing

1. Login with default credentials
2. Check initial balance (Rs 10,000)
3. Make a deposit
4. Make a withdrawal (ensure sufficient balance)
5. View transaction history
6. Logout and verify redirect to login page

## 🛠️ Customization

To change default credentials, edit `js/init.js`:
```javascript
const credentials = {
    username: 'your-email@example.com',
    password: 'your-password'
};
```

To change initial balance, edit `js/init.js`:
```javascript
localStorage.setItem('balance', '50000'); // Change to desired amount
```

## 📱 Browser Compatibility

Works on all modern browsers:
- Chrome (recommended)
- Firefox
- Safari
- Edge
- Opera

## ⚠️ Important Notes

- This is a **frontend-only simulation** - no real banking functionality
- Data is stored locally in your browser
- Clearing browser data will reset all transactions and balance
- Not suitable for production use - for demonstration purposes only

---

**Enjoy your banking simulation! 🏦**

