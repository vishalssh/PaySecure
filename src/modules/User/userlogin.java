package modules.User;

import util.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class userlogin extends User {

    private Scanner sc = new Scanner(System.in);

    public boolean login() {
        System.out.println("=========== User Login ===========");

        System.out.print("Enter Username: ");
        String inputUsername = sc.nextLine().trim();

        System.out.print("Enter Password: ");
        String inputPassword = sc.nextLine().trim();

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
                userId = rs.getInt("user_id");
                this.username = rs.getString("username");
                fullName = rs.getString("full_name");
                email = rs.getString("email");
                mobileNumber = rs.getString("mobile_number");
                upiId = rs.getString("upi_id");
                accountNumber = rs.getString("account_number");
                role = rs.getString("role");

                System.out.println("Login successful! Welcome, " + fullName);
                return true;
            } else {
                System.out.println("Invalid username or password!");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Error during login: " + e.getMessage());
            return false;
        }
    }

    public void showProfile() {
        System.out.println("=========== User Profile ===========");
        System.out.println("Username: " + username);
        System.out.println("Full Name: " + fullName);
        System.out.println("Email: " + email);
        System.out.println("Mobile: " + mobileNumber);
        System.out.println("UPI ID: " + upiId);
        System.out.println("Account Number: " + accountNumber);
        System.out.println("Role: " + role);
    }

    public void checkBalance() {
        String query = "SELECT balance FROM users WHERE user_id = ?";

        try (Connection cn = DBConnection.getConnection();
                PreparedStatement pstmt = cn.prepareStatement(query)) {

            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                double balance = rs.getDouble("balance");
                System.out.println("\n=========== Balance Information ===========");
                System.out.println("Account Number: " + accountNumber);
                System.out.println("Current Balance: " + balance);
                System.out.println("=========================================");
            } else {
                System.out.println("Unable to retrieve balance!");
            }
        } catch (SQLException e) {
            System.err.println("Error while checking balance: " + e.getMessage());
        }
    }

}
