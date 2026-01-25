package modules.User;

import util.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class userlogin {
    private int userId;
    private String username;
    private String fullName;
    private String email;
    private String mobileNumber;
    private String upiId;
    private String accountNumber;
    private String role;

    private Scanner scanner = new Scanner(System.in);

    public boolean login() {
        System.out.println("=========== User Login ===========");

        System.out.print("Enter Username: ");
        String inputUsername = scanner.nextLine().trim();

        System.out.print("Enter Password: ");
        String inputPassword = scanner.nextLine().trim();

        return authenticateUser(inputUsername, inputPassword);
    }

    private boolean authenticateUser(String username, String password) {
        String query = "SELECT user_id, username, full_name, email, mobile_number, upi_id, account_number, role FROM users WHERE username = ? AND password = ?";

        try (Connection cn = DBConnection.getConnection();
                PreparedStatement pstmt = cn.prepareStatement(query)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                this.userId = rs.getInt("user_id");
                this.username = rs.getString("username");
                this.fullName = rs.getString("full_name");
                this.email = rs.getString("email");
                this.mobileNumber = rs.getString("mobile_number");
                this.upiId = rs.getString("upi_id");
                this.accountNumber = rs.getString("account_number");
                this.role = rs.getString("role");

                System.out.println("\n Login successful! Welcome, " + this.fullName + "!");
                return true;
            } else {
                System.out.println("\n Invalid username or password!");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Error during login: " + e.getMessage());
            return false;
        }
    }

    public void showProfile() {
        System.out.println("=========== User Profile ===========");
        System.out.println("Username: " + this.username);
        System.out.println("Full Name: " + this.fullName);
        System.out.println("Email: " + this.email);
        System.out.println("Mobile: " + this.mobileNumber);
        System.out.println("UPI ID: " + this.upiId);
        System.out.println("Account Number: " + this.accountNumber);
        System.out.println("Role: " + this.role);
    }

    // Getters
    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getFullName() {
        return fullName;
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
