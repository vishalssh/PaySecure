package modules.Transaction;

import util.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class TransferService {

    Scanner sc = new Scanner(System.in);

    public void transferMoney(int senderId) throws SQLException {
        System.out.println("=========== Transfer Money ===========");

        System.out.print("Enter user UPI ID or Account Number: ");
        String RecUser = sc.next().trim();

        int receiverId = findUser(RecUser);

        System.out.print("Enter amount to transfer: ");
        double amount;
        try {
            amount = Double.parseDouble(sc.next().trim());
        } catch (NumberFormatException e) {
            System.out.println("Invalid amount!");
            return;
        }

        if (amount <= 0) {
            System.out.println("Amount must be greater than 0!");
            return;
        }

        if (!balanceCheck(senderId, amount)) {
            System.out.println("Insufficient balance!");
            return;
        }

        if (performTransfer(senderId, receiverId, amount)) {
            System.out.println("=========================================");
            System.out.println("Transfer successful!");
            System.out.println("Amount transferred: " + amount);
            System.out.println("=========================================");
        } else {
            System.out.println("Transfer failed! Please try again.");
        }
    }

    private int findUser(String id) throws SQLException {
        String query = "SELECT user_id FROM users WHERE upi_id = ? OR account_number = ?";

        try (Connection cn = DBConnection.getConnection();
                PreparedStatement pstmt = cn.prepareStatement(query)) {

            pstmt.setString(1, id);
            pstmt.setString(2, id);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("user_id");
            }
        }
        return -1;
    }

    private boolean balanceCheck(int userId, double amount) throws SQLException {
        String query = "SELECT balance FROM users WHERE user_id = ?";

        try (Connection cn = DBConnection.getConnection();
                PreparedStatement pstmt = cn.prepareStatement(query)) {

            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                double balance = rs.getDouble("balance");
                return balance >= amount;
            }
        }
        return false;
    }

    private boolean performTransfer(int senderId, int receiverId, double amount) throws SQLException {

        Connection cn = DBConnection.getConnection();

        String deductQuery = "UPDATE users SET balance = balance - ? WHERE user_id = ?";
        try (PreparedStatement pstmt = cn.prepareStatement(deductQuery)) {
            pstmt.setDouble(1, amount);
            pstmt.setInt(2, senderId);
            pstmt.executeUpdate();
        }

        String addQuery = "UPDATE users SET balance = balance + ? WHERE user_id = ?";
        try (PreparedStatement pstmt = cn.prepareStatement(addQuery)) {
            pstmt.setDouble(1, amount);
            pstmt.setInt(2, receiverId);
            pstmt.executeUpdate();
        }

        String transactionQuery = "INSERT INTO transactions (sender_id, receiver_id, amount, transaction_type) VALUES (?, ?, ?, 'TRANSFER')";
        try (PreparedStatement pstmt = cn.prepareStatement(transactionQuery)) {
            pstmt.setInt(1, senderId);
            pstmt.setInt(2, receiverId);
            pstmt.setDouble(3, amount);
            pstmt.executeUpdate();
        }
        return true;
    }

    public void showHistory(int userId) throws SQLException {
        System.out.println("=========== Transaction History ===========");

        String query = "SELECT t.transaction_id, t.sender_id, t.receiver_id, t.amount, t.transaction_type, t.created_at, "+
                "sender.username AS sender_name, receiver.username AS receiver_name " +
                "FROM transactions t " +
                "LEFT JOIN users sender ON t.sender_id = sender.user_id " +
                "LEFT JOIN users receiver ON t.receiver_id = receiver.user_id " +
                "WHERE t.sender_id = ? OR t.receiver_id = ? " +
                "ORDER BY t.created_at DESC LIMIT 20";

        try (Connection cn = DBConnection.getConnection();
                PreparedStatement pstmt = cn.prepareStatement(query)) {

            pstmt.setInt(1, userId);
            pstmt.setInt(2, userId);

            ResultSet rs = pstmt.executeQuery();

            boolean transferred = false;
            while (rs.next()) {
                transferred = true;
                int transactionId = rs.getInt("transaction_id");
                int senderId = rs.getInt("sender_id");
                int receiverId = rs.getInt("receiver_id");
                double amount = rs.getDouble("amount");
                String type = rs.getString("transaction_type");
                String createdAt = rs.getString("created_at");
                String senderName = rs.getString("sender_name");
                String receiverName = rs.getString("receiver_name");

                System.out.println("-------------------------------------------");
                System.out.println("Transaction ID: " + transactionId);
                System.out.println("Date: " + createdAt);
                System.out.println("Type: " + type);

                if (senderId == userId) {
                    System.out.println("To: " + (receiverName != null ? receiverName : "Unknown"));
                    System.out.println("Amount: " + amount + " (Sent)");
                } else {
                    System.out.println("From: " + (senderName != null ? senderName : "Unknown"));
                    System.out.println("Amount: " + amount + " (Received)");
                }
            }

            if (!transferred) {
                System.out.println("No transactions found.");
            }

        } catch (SQLException e) {
            System.err.println("Error fetching transaction history: " + e.getMessage());
        }
    }
}
