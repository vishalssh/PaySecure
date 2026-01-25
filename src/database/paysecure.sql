-- Create Database
CREATE DATABASE IF NOT EXISTS paysecure;

-- Use the database
USE paysecure;

-- Create users table
CREATE TABLE users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    mobile_number VARCHAR(15) UNIQUE NOT NULL,
    upi_id VARCHAR(50) UNIQUE NOT NULL,
    account_number VARCHAR(20) UNIQUE NOT NULL,
    role VARCHAR(10) DEFAULT 'USER'
);