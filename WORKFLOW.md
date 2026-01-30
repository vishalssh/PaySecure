# PaySecure - Project Workflow Documentation

## Table of Contents
1. [Overview](#overview)
2. [System Architecture](#system-architecture)
3. [Database Schema](#database-schema)
4. [User Workflows](#user-workflows)
5. [Admin Workflows](#admin-workflows)
6. [Module Details](#module-details)
7. [Data Flow](#data-flow)

---

## Overview

**PaySecure** is a Digital Money Management System built with Java and MySQL that enables users to manage digital wallets, transfer money, and maintain transaction histories. The system provides separate interfaces for regular users and administrators.

### Key Features
- User Registration & Authentication
- Digital Wallet Management
- Money Transfer via UPI ID or Account Number
- Transaction History Tracking
- Admin Panel for User and Transaction Management

---

## System Architecture

### Technology Stack
- **Backend**: Java (Console-based application)
- **Database**: MySQL 
- **JDBC Driver**: MySQL Connector/J

### Module Structure
```
PaySecure/
├── src/
│   ├── paySecure.java          # Main application entry point
│   ├── util/
│   │   └── DBConnection.java   # Database connection utility
│   ├── modules/
│   │   ├── User/
│   │   │   ├── User.java       # User registration
│   │   │   └── userlogin.java  # User authentication & profile
│   │   ├── Admin/
│   │   │   └── Admin.java      # Admin operations
│   │   ├── Transaction/
│   │   │   └── TransferService.java  # Money transfer & history
│   │   └── Wallet/
│   │       ├── Wallet.java     # Wallet entity
│   │       ├── WalletDAO.java  # Wallet data access
│   │       └── walletMenu.java # Wallet menu interface
│   └── database/
│       └── paysecure.sql       # Database schema
```

---

## Database Schema

### Tables

#### 1. `users` Table
Stores all user information including credentials and balances.

| Column | Type | Description |
|--------|------|-------------|
| user_id | INT (PK, AUTO_INCREMENT) | Unique user identifier |
| username | VARCHAR(50) UNIQUE | Username for login |
| password | VARCHAR(255) | User password (plain text) |
| full_name | VARCHAR(100) | User's full name |
| email | VARCHAR(100) | Email address |
| mobile_number | VARCHAR(15) UNIQUE | 10-digit mobile number |
| upi_id | VARCHAR(50) UNIQUE | Auto-generated UPI ID (username@paysecure) |
| account_number | VARCHAR(20) UNIQUE | Auto-generated 12-digit account number |
| role | VARCHAR(10) | USER or ADMIN |
| balance | DECIMAL(15,2) | Main account balance |
| created_at | TIMESTAMP | Registration timestamp |

#### 2. `wallet` Table
Stores separate wallet balance for each user.

| Column | Type | Description |
|--------|------|-------------|
| wallet_id | INT (PK, AUTO_INCREMENT) | Unique wallet identifier |
| user_id | INT UNIQUE (FK) | Reference to users table |
| balance | DECIMAL(10,2) | Wallet balance |

#### 3. `transactions` Table
Records all money transfers between users.

| Column | Type | Description |
|--------|------|-------------|
| transaction_id | INT (PK, AUTO_INCREMENT) | Unique transaction identifier |
| sender_id | INT (FK) | Sender's user_id |
| receiver_id | INT (FK) | Receiver's user_id |
| amount | DECIMAL(15,2) | Transaction amount |
| transaction_type | VARCHAR(20) | Type (default: TRANSFER) |
| created_at | TIMESTAMP | Transaction timestamp |

---

## User Workflows

### 1. User Registration Flow
```
START
  ↓
Display Registration Form
  ↓
User enters:
  - Username (min 4 chars)
  - Password (min 6 chars)
  - Full Name (alphabets only)
  - Email (validated format)
  - Mobile Number (10 digits)
  ↓
System auto-generates:
  - UPI ID: username@paysecure
  - Account Number: 12-digit random number
  - Role: USER
  - Balance: 0.00
  ↓
Save to database (users table)
  ↓
Display registration success
  ↓
END
```

**Implementation**: `User.java` → `registerUser()` → `takeUserInput()` → `saveUser()`

---

### 2. User Login Flow
```
START
  ↓
Display Login Form
  ↓
User enters:
  - Username
  - Password
  ↓
Query database for matching credentials
  ↓
If valid credentials:
  - Load user profile data
  - Display "Login successful"
  - Show User Main Menu
  ↓
If invalid:
  - Display "Invalid username or password"
  - Return to Auth Menu
  ↓
END
```

**Implementation**: `userlogin.java` → `login()` → `authenticateUser()`

---

### 3. User Main Menu Flow

After successful login, users can access:

#### a. View Profile
- Displays: Username, Full Name, Email, Mobile, UPI ID, Account Number, Role

#### b. Check Balance
- Queries `users` table for current balance
- Displays account number and balance amount

#### c. Add Balance
```
User enters amount
  ↓
Validate amount > 0
  ↓
UPDATE users SET balance = balance + amount WHERE user_id = ?
  ↓
Display success message
```

#### d. Wallet (Sub-menu)
```
1. Add Money to Wallet
   - Enter amount (max ₹100,000)
   - Create wallet if not exists
   - UPDATE wallet SET balance = balance + amount
   
2. Check Wallet Balance
   - Display current wallet balance
   
3. Exit Wallet
   - Return to main menu
```

**Implementation**: `walletMenu.java` → `WalletDAO.java`

#### e. Transfer Money
```
START Transfer
  ↓
User enters:
  - Recipient UPI ID or Account Number
  - Amount to transfer
  ↓
Validate:
  - Amount > 0
  - Recipient exists
  - Sender has sufficient balance
  ↓
Perform Transfer (Transaction):
  1. Deduct from sender's balance
  2. Add to receiver's balance
  3. Insert transaction record
  ↓
Display success message
  ↓
END
```

**Implementation**: `TransferService.java` → `transferMoney()` → `performTransfer()`

#### f. Transaction History
- Displays last 20 transactions where user is sender or receiver
- Shows: Transaction ID, Date, Type, Amount (Sent/Received), Other party

#### g. Logout
- Returns to Authentication Menu

---

## Admin Workflows

### 1. Admin Login Flow
```
START
  ↓
Display Admin Login Form
  ↓
Admin enters:
  - Username
  - Password
  ↓
Query: SELECT * FROM users WHERE username=? AND password=? AND role='ADMIN'
  ↓
If valid:
  - Display "Admin login successful"
  - Show Admin Menu
  ↓
If invalid:
  - Display "Invalid admin credentials"
  - Return to Auth Menu
  ↓
END
```

**Implementation**: `Admin.java` → `login()`

---

### 2. Admin Menu Flow

#### a. View Users
```
Query all users with role='USER'
  ↓
Display for each user:
  - User ID
  - Full Name
  - Role
  - Balance
```

#### b. Transaction History
```
Query last 20 transactions
  ↓
JOIN with users table to get sender/receiver names
  ↓
Display:
  - Transaction ID
  - From (sender username)
  - To (receiver username)
  - Amount
  - Date
```

#### c. Remove User
```
Admin enters username
  ↓
Find user_id for username (where role='USER')
  ↓
If found:
  - DELETE FROM users WHERE user_id=?
  - Cascade deletes wallet and transaction records
  - Display success message
  ↓
If not found:
  - Display "User not found"
```

#### d. Logout
- Returns to Authentication Menu

---

## Module Details

### 1. Main Application Module (`paySecure.java`)
- **Entry Point**: `main()` method
- **Authentication Menu**: Registration, User Login, Admin Login, Exit
- **Menu Routing**: Delegates to respective modules based on user choice

### 2. Database Utility Module (`DBConnection.java`)
- **Connection Details**: localhost:3306/paysecure
- **Credentials**: root/root (default)
- **Driver**: MySQL Connector/J (com.mysql.cj.jdbc.Driver)
- **Method**: `getConnection()` - Returns Connection object

### 3. User Module
**User.java**
- User registration with input validation
- Auto-generation of UPI ID and Account Number
- Database persistence

**userlogin.java**
- Extends User class
- User authentication
- Profile management (view, check balance, add balance)

### 4. Admin Module (`Admin.java`)
- Admin authentication (role-based)
- User management (view, remove)
- Transaction monitoring

### 5. Transaction Module (`TransferService.java`)
- Money transfer between users
- Balance validation
- Transaction recording
- Transaction history retrieval

### 6. Wallet Module
**Wallet.java** - Entity class with getters/setters

**WalletDAO.java** - Data Access Object
- `createWallet()` - Creates wallet if not exists
- `addMoney()` - Adds money to wallet
- `getBalance()` - Retrieves wallet balance

**walletMenu.java** - User interface for wallet operations

---

## Data Flow

### Complete Transaction Flow Example

```
User A wants to transfer ₹500 to User B
  ↓
1. USER AUTHENTICATION
   - User A logs in with username/password
   - System loads User A's profile (user_id, balance, etc.)
  ↓
2. INITIATE TRANSFER
   - User A selects "Transfer Money" from menu
   - Enters User B's UPI ID: "userb@paysecure"
   - Enters amount: 500
  ↓
3. VALIDATION
   - System finds User B by UPI ID → user_id = 2
   - Checks User A's balance >= 500
   - Validates amount > 0
  ↓
4. DATABASE TRANSACTION
   a. UPDATE users SET balance = balance - 500 WHERE user_id = 1 (User A)
   b. UPDATE users SET balance = balance + 500 WHERE user_id = 2 (User B)
   c. INSERT INTO transactions (sender_id, receiver_id, amount, type)
      VALUES (1, 2, 500, 'TRANSFER')
  ↓
5. CONFIRMATION
   - Display "Transfer successful!"
   - Show amount transferred
   - Update available in transaction history
  ↓
END
```

### Wallet vs Main Account Balance

**Two Separate Balances:**
1. **Main Account Balance** (`users.balance`)
   - Used for money transfers
   - Can be checked and topped up from main menu
   
2. **Wallet Balance** (`wallet.balance`)
   - Separate wallet feature
   - Currently operates independently
   - No automatic sync with main balance

---

## Security Considerations

⚠️ **Current Implementation Notes:**
1. Passwords stored in plain text (not hashed)
2. No SQL injection protection (vulnerable PreparedStatements usage)
3. No transaction rollback mechanism for failed transfers
4. Database credentials hardcoded in source

**Recommended Improvements:**
- Implement password hashing (BCrypt, PBKDF2)
- Add SQL injection protection
- Implement database transactions with rollback
- Use environment variables for DB credentials
- Add session management and timeout
- Implement rate limiting for transfers

---

## Setup Instructions

### Prerequisites
- Java JDK 8+
- MySQL Server 5.7+
- MySQL Connector/J (JDBC Driver)

### Database Setup
```sql
# Run the SQL script
mysql -u root -p < src/database/paysecure.sql
```

### Running the Application
```bash
# Compile
javac -cp .:mysql-connector-java.jar src/paySecure.java

# Run
java -cp .:mysql-connector-java.jar paySecure
```

---

## Conclusion

PaySecure provides a complete digital payment ecosystem with user management, wallet functionality, money transfers, and administrative controls. The modular architecture separates concerns effectively, making the codebase maintainable and extensible.

**Key Workflows:**
- User Registration → Login → Transfer Money → Check History
- Admin Login → Monitor Users → View Transactions → Remove Users
- Wallet: Create → Add Money → Check Balance
