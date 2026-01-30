# PaySecure - Digital Money Management System

A Java-based console application for managing digital wallets, money transfers, and transaction tracking with separate user and admin interfaces.

## Features

- 👤 **User Management**: Registration with auto-generated UPI IDs and account numbers
- 💰 **Digital Wallet**: Separate wallet balance management
- 💸 **Money Transfer**: Transfer funds via UPI ID or Account Number
- 📊 **Transaction History**: Track all sent and received transactions
- 🔐 **Admin Panel**: User management and transaction monitoring
- 🏦 **Balance Management**: Add funds to main account and wallet

## Documentation

📖 **[Complete Workflow Documentation](WORKFLOW.md)** - Detailed documentation covering:
- System architecture and module structure
- Database schema and relationships
- User and admin workflows with diagrams
- Step-by-step process flows
- Setup and installation instructions

## Quick Start

### Prerequisites
- Java JDK 8+
- MySQL Server 5.7+
- MySQL Connector/J (JDBC Driver)

### Setup
1. Create the database:
   ```bash
   mysql -u root -p < src/database/paysecure.sql
   ```

2. Compile and run:
   
   **Linux/Mac:**
   ```bash
   javac -cp .:mysql-connector-java.jar src/paySecure.java
   java -cp .:mysql-connector-java.jar paySecure
   ```
   
   **Windows:**
   ```cmd
   javac -cp .;mysql-connector-java.jar src/paySecure.java
   java -cp .;mysql-connector-java.jar paySecure
   ```

## Project Structure

```
PaySecure/
├── src/
│   ├── paySecure.java                    # Main application
│   ├── util/DBConnection.java            # Database connection
│   ├── modules/
│   │   ├── User/                         # User registration & login
│   │   ├── Admin/                        # Admin operations
│   │   ├── Transaction/                  # Money transfers
│   │   └── Wallet/                       # Wallet management
│   └── database/paysecure.sql            # Database schema
└── WORKFLOW.md                            # Complete workflow documentation
```

## Main Workflows

### For Users
1. Register → Auto-generated UPI ID and Account Number
2. Login → Access main menu
3. Add balance to account or wallet
4. Transfer money using UPI ID or Account Number
5. View transaction history

### For Admins
1. Admin Login → Access admin panel
2. View all users and their balances
3. Monitor all transactions
4. Remove users from the system

## Technology Stack

- **Language**: Java
- **Database**: MySQL
- **JDBC Driver**: MySQL Connector/J
- **Architecture**: Console-based MVC pattern

## License

This project is open source and available for educational purposes.
