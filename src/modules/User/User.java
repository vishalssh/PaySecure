package modules.User;

import util.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;
import java.util.Scanner;

public class User {
    private int userId;
    private String username;
    private String password;
    private String fullName;
    private String email;
    private String mobileNumber;
    private String upiId;
    private String accountNumber;
    private String role;

    private Scanner scanner = new Scanner(System.in);

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
        this.username = scanner.nextLine().trim();

        System.out.print("Enter Password: ");
        this.password = scanner.nextLine().trim();

        System.out.print("Enter Full Name: ");
        this.fullName = scanner.nextLine().trim();

        System.out.print("Enter Email: ");
        this.email = scanner.nextLine().trim();

        System.out.print("Enter Mobile Number: ");
        this.mobileNumber = scanner.nextLine().trim();

        // Auto-generate UPI ID and Account Number
        this.upiId = generateUpiId(this.username);
        this.accountNumber = generateAccountNumber();
        this.role = "USER";


        System.out.println("UPI ID: " + this.upiId);
        System.out.println("Account Number: " + this.accountNumber);
    }

    // Save user to database
    public boolean saveUser() {
        String UI = "INSERT INTO users (username, password, full_name, email, mobile_number, upi_id, account_number, role) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection cn = DBConnection.getConnection();
                PreparedStatement pstmt = cn.prepareStatement(UI)) {

            pstmt.setString(1, this.username);
            pstmt.setString(2, this.password);
            pstmt.setString(3, this.fullName);
            pstmt.setString(4, this.email);
            pstmt.setString(5, this.mobileNumber);
            pstmt.setString(6, this.upiId);
            pstmt.setString(7, this.accountNumber);
            pstmt.setString(8, this.role);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("\n User registered successfully!");
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error saving user: " + e.getMessage());
        }
        return false;
    }

    public void registerUser() {
        takeUserInput();
        saveUser();
    }

    public void showUser() {
        System.out.println("=========== User Details ===========");
        System.out.println("Username: " + this.username);
        System.out.println("Full Name: " + this.fullName);
        System.out.println("Email: " + this.email);
        System.out.println("Mobile: " + this.mobileNumber);
        System.out.println("UPI ID: " + this.upiId);
        System.out.println("Account Number: " + this.accountNumber);
        System.out.println("Role: " + this.role);
    }

    // Getters
    public String getUsername() {
        return username;
    }

    public String getUpiId() {
        return upiId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getRole() {
        return role;
    }
}
