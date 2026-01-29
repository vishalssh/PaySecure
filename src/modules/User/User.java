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

    // Generate unique UPI ID
    private String generateUpiId(String username) {
        return username.toLowerCase() + "@paysecure";
    }

    // Generate unique Account Number (12 digits)
    private String generateAccountNumber() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 12; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    // Take user input
    public void takeUserInput() {
        System.out.println("=========== User Registration ===========");

        System.out.print("Enter Username: ");
        username = sc.nextLine().trim();

        System.out.print("Enter Password: ");
        password = sc.nextLine().trim();

        System.out.print("Enter Full Name: ");
        fullName = sc.nextLine().trim();

        System.out.print("Enter Email: ");
        email = sc.nextLine().trim();

        System.out.print("Enter Mobile Number: ");
        mobileNumber = sc.nextLine().trim();

        // Auto-generate UPI ID and Account Number
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
