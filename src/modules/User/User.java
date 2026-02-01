package modules.User;

import util.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;
import java.util.Scanner;

public class User {
    public int userId;
    String username;
    String password;
    String fullName;
    String email;
    String mobileNumber;
    String upiId;
    String accountNumber;
    String role;

    Scanner sc = new Scanner(System.in);

    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.com$");
    }

    private boolean isValidUsername(String username) {
        return username.matches("^[A-Za-z0-9_]{4,20}$");
    }

    private boolean isValidMobile(String mobile) {
        return mobile.matches("\\d{10}");
    }

    private boolean isValidName(String name) {
        return name.matches("[A-Za-z ]+");
    }

    private String generateUpiId(String username) {
        return username.toLowerCase() + "@paysecure";
    }

    private String generateAccountNumber() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 12; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    public void takeUserInput() {
        System.out.println("=========== User Registration ===========");

        do {
            System.out.print("Enter Username: ");
            username = sc.nextLine().trim();
        } while (!isValidUsername(username));

        do {
            System.out.print("Enter Password (min 6 chars): ");
            password = sc.nextLine().trim();
        } while (password.length() < 6);

        do {
            System.out.print("Enter Full Name: ");
            fullName = sc.nextLine().trim();
        } while (!isValidName(fullName));

        do {
            System.out.print("Enter Email: ");
            email = sc.nextLine().trim();
        } while (!isValidEmail(email));

        do {
            System.out.print("Enter Mobile Number (10 digits): ");
            mobileNumber = sc.nextLine().trim();
        } while (!isValidMobile(mobileNumber));

        upiId = generateUpiId(username);
        accountNumber = generateAccountNumber();
        role = "USER";

        System.out.println("UPI ID: " + upiId);
        System.out.println("Account Number: " + accountNumber);
    }

    // Save user to database
    public void saveUser() throws SQLException {
        String UI = "insert into users (username, password, full_name, email, mobile_number, upi_id, account_number, role) values (?, ?, ?, ?, ?, ?, ?, ?)";
        Connection cn = DBConnection.getConnection();
        PreparedStatement pstmt = cn.prepareStatement(UI);
        try {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, fullName);
            pstmt.setString(4, email);
            pstmt.setString(5, mobileNumber);
            pstmt.setString(6, upiId);
            pstmt.setString(7, accountNumber);
            pstmt.setString(8, role);

            int rowsAf = pstmt.executeUpdate();
            if (rowsAf > 0) {
                System.out.println("User registered successfully!");
            }
        } catch (SQLException e) {
            System.err.println("Error saving user: " + e.getMessage());
        }
    }

    public void registerUser() throws SQLException {
        takeUserInput();
        saveUser();
    }

    public void showUser() {
        System.out.println("=========== User Details ===========");
        System.out.println("Username: " + username);
        System.out.println("Full Name: " + fullName);
        System.out.println("Email: " + email);
        System.out.println("Mobile: " + mobileNumber);
        System.out.println("UPI ID: " + upiId);
        System.out.println("Account Number: " + accountNumber);
        System.out.println("Role: " + role);
    }

}
