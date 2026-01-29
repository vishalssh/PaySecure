package modules.Wallet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import util.DBConnection;

public class WalletDAO {

    // Create wallet if not exists
    public void createWallet(int userId) {
        try (Connection con = DBConnection.getConnection()) {

            String checkSql = "SELECT wallet_id FROM wallet WHERE user_id = ?";
            PreparedStatement checkPs = con.prepareStatement(checkSql);
            checkPs.setInt(1, userId);
            ResultSet rs = checkPs.executeQuery();

            if (!rs.next()) {
                String insertSql = "INSERT INTO wallet(user_id, balance) VALUES (?, 0)";
                PreparedStatement ps = con.prepareStatement(insertSql);
                ps.setInt(1, userId);
                ps.executeUpdate();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Add money
    public void addMoney(int userId, double amount) {

        // Business validation
        if (amount <= 0) {
            System.out.println("Amount must be greater than 0.");
            return;
        }

        try (Connection con = DBConnection.getConnection()) {

            // Ensure wallet exists
            createWallet(userId);

            String sql = "UPDATE wallet SET balance = balance + ? WHERE user_id = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setDouble(1, amount);
            ps.setInt(2, userId);

            int rowsUpdated = ps.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Money added successfully.");
            } else {
                System.out.println("Failed to add money.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Get balance
    public double getBalance(int userId) {
        double balance = 0;

        try (Connection con = DBConnection.getConnection()) {

            String sql = "SELECT balance FROM wallet WHERE user_id = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                balance = rs.getDouble("balance");
            }
            rs.close();
            ps.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return balance;
    }
}
