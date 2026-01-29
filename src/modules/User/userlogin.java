package modules.User;

import util.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class userlogin extends User {

    Scanner sc = new Scanner(System.in);

    public boolean login() throws SQLException {
        System.out.println("=========== User Login ===========");

        System.out.print("Enter Username: ");
        String inputuser = sc.next().trim();

        System.out.print("Enter Password: ");
        String inputpass = sc.next().trim();

        return authenticateUser(inputuser, inputpass);
    }

    public boolean authenticateUser(String username, String password) throws SQLException {

        String query = "select user_id, username, full_name, email, mobile_number, upi_id, account_number, role FROM users where username = ? AND password = ?";
        Connection cn = DBConnection.getConnection();
        PreparedStatement pstmt = cn.prepareStatement(query);
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

    public void checkBalance() throws SQLException {
        String query = "SELECT balance FROM users WHERE user_id = ?";
        Connection cn = DBConnection.getConnection();
        PreparedStatement pstmt = cn.prepareStatement(query);

        pstmt.setInt(1, userId);
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            double balance = rs.getDouble("balance");
            System.out.println("=========== Balance Information ===========");
            System.out.println("Account Number: " + accountNumber);
            System.out.println("Current Balance: " + balance);
        } else {
            System.out.println("Unable to retrieve balance!");
        }

    }

    public void addBalance() throws SQLException {
        System.out.println("=========== Add Balance ===========");

        System.out.print("Enter amount to add: ");
        try {
            double amount = Double.parseDouble(sc.nextLine().trim());

            if (amount <= 0) {
                System.out.println("Amount must be greater than 0!");
                return;
            }

            String query = "UPDATE users set balance = balance + ? where user_id = ?";
            Connection cn = DBConnection.getConnection();
            PreparedStatement pstmt = cn.prepareStatement(query);
            pstmt.setDouble(1, amount);
            pstmt.setInt(2, userId);

            int rowsAf = pstmt.executeUpdate();

            if (rowsAf > 0) {
                System.out.println("Balance added successfully!");
                System.out.println("Amount Added: " + amount);
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount! Please enter a valid number.");
        }
    }

}
