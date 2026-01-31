package modules.Admin;

import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Admin {
    private final Scanner sc = new Scanner(System.in);

    public boolean login() throws SQLException {
        System.out.println("=========== Admin Login ==============");
        System.out.print("Enter Username: ");
        String username = sc.next().trim();
        System.out.print("Enter Password: ");
        String password = sc.next().trim();

        String query = "SELECT user_id FROM users WHERE username = ? AND password = ? AND role = 'ADMIN'";
        Connection cn = DBConnection.getConnection();
        PreparedStatement pstmt = cn.prepareStatement(query);
        pstmt.setString(1, username);
        pstmt.setString(2, password);

        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            System.out.println("Admin login successful!");
            return true;
        }

        System.out.println("Invalid admin credentials!");
        return false;
    }

    public void viewUsers() throws SQLException {
        System.out.println("=========== All Users ===========");

        String query = "select user_id, username,full_name, role from users where role = 'USER' order by user_id";
        Connection cn = DBConnection.getConnection();
        PreparedStatement pstmt = cn.prepareStatement(query);
        ResultSet rs = pstmt.executeQuery();

        while (rs.next()) {
            System.out.println("-------------------------------------------");
            System.out.println("User ID: " + rs.getInt("user_id"));
            System.out.println("Username: " + rs.getString("username"));
            System.out.println("Name: " + rs.getString("full_name"));
            System.out.println("Role: " + rs.getString("role"));
        }
    }

    public void viewAllTransactions() throws SQLException {
        System.out.println("============= Transaction History ===========");

        String query = "SELECT t.transaction_id, t.amount, t.created_at, " +
                "sender.username AS sender_name, receiver.username AS receiver_name " +
                "FROM transactions t " +
                "JOIN users sender ON t.sender_id = sender.user_id " +
                "JOIN users receiver ON t.receiver_id = receiver.user_id " +
                "ORDER BY t.created_at DESC LIMIT 20";

        Connection cn = DBConnection.getConnection();
        PreparedStatement pstmt = cn.prepareStatement(query);
        ResultSet rs = pstmt.executeQuery();

        int Transfer = 0;
        while (rs.next()) {
            Transfer = 1;
            System.out.println("-------------------------------------------");
            System.out.println("Transaction ID: " + rs.getInt("transaction_id"));
            System.out.println("From: " + rs.getString("sender_name"));
            System.out.println("To: " + rs.getString("receiver_name"));
            System.out.println("Amount: " + rs.getDouble("amount"));
            System.out.println("Date: " + rs.getString("created_at"));
        }

        if (Transfer > 0) {
            System.out.println("No transactions found!");
        }
    }

    public void removeUser() throws SQLException {
        System.out.println("=========== Remove User ==============");
        System.out.print("Enter Username: ");
        String username = sc.next().trim();

        int userId = findUser(username);
        if (userId == -1) {
            System.out.println("User not found!");
            return;
        }

        Connection cn = DBConnection.getConnection();
        String deleteUser = "delete from users where user_id = ?";
        PreparedStatement userStmt = cn.prepareStatement(deleteUser);
        userStmt.setInt(1, userId);
        int rows = userStmt.executeUpdate();

        if (rows > 0) {
            System.out.println("User removed successfully: " + username);
        } else {
            System.out.println("Failed to remove user!");
        }
    }

    private int findUser(String username) throws SQLException {
        String query = "select user_id from users where username = ? AND role = 'USER'";
        Connection cn = DBConnection.getConnection();
        PreparedStatement pstmt = cn.prepareStatement(query);
        pstmt.setString(1, username);

        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            return rs.getInt("user_id");
        }
        return -1;
    }
}
