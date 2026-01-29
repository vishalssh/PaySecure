
package modules.Wallet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import util.DBConnection;

public class WalletDAO {

    // Create wallet if not exists
    public void createWallet(int userId) {
        try {
            Connection con = DBConnection.getConnection();
            String sql = "INSERT INTO wallet(user_id, balance) VALUES (?, 0)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.executeUpdate();
        } catch (Exception e) {
            // wallet already exists
        }
    }

    // Add money to wallet
    public void addMoney(int userId, double amount) {
        try {
            Connection con = DBConnection.getConnection();
            String sql = "UPDATE wallet SET balance = balance + ? WHERE user_id = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setDouble(1, amount);
            ps.setInt(2, userId);
            ps.executeUpdate();
            System.out.println("Money added successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Get wallet balance
    public double getBalance(int userId) {
        double balance = 0;
        try {
            Connection con = DBConnection.getConnection();
            String sql = "SELECT balance FROM wallet WHERE user_id = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                balance = rs.getDouble("balance");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return balance;
    }
}
