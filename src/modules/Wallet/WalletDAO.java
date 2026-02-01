package modules.Wallet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import util.DBConnection;

public class WalletDAO {

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

    public void addMoney(int userId, double amount) {

        if (amount <= 0) {
            System.out.println("Amount must be greater than 0.");
            return;
        }

        Connection con = null;
        try {
            con = DBConnection.getConnection();
            con.setAutoCommit(false);

            ensureWallet(con, userId);

            double userBalance = getUserBalance(con, userId);
            if (userBalance < amount) {
                System.out.println("Insufficient balance in user account.");
                con.rollback();
                return;
            }

            String deductSql = "UPDATE users SET balance = balance - ? WHERE user_id = ?";
            try (PreparedStatement ps = con.prepareStatement(deductSql)) {
                ps.setDouble(1, amount);
                ps.setInt(2, userId);
                ps.executeUpdate();
            }

            String addSql = "UPDATE wallet SET balance = balance + ? WHERE user_id = ?";
            try (PreparedStatement ps = con.prepareStatement(addSql)) {
                ps.setDouble(1, amount);
                ps.setInt(2, userId);
                int rowsUpdated = ps.executeUpdate();
                if (rowsUpdated > 0) {
                    con.commit();
                    System.out.println("Money added successfully.");
                } else {
                    con.rollback();
                    System.out.println("Failed to add money.");
                }
            }

        } catch (Exception e) {
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            if (con != null) {
                try {
                    con.setAutoCommit(true);
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void ensureWallet(Connection con, int userId) throws SQLException {
        String checkSql = "SELECT wallet_id FROM wallet WHERE user_id = ?";
        try (PreparedStatement checkPs = con.prepareStatement(checkSql)) {
            checkPs.setInt(1, userId);
            ResultSet rs = checkPs.executeQuery();
            if (!rs.next()) {
                String insertSql = "INSERT INTO wallet(user_id, balance) VALUES (?, 0)";
                try (PreparedStatement ps = con.prepareStatement(insertSql)) {
                    ps.setInt(1, userId);
                    ps.executeUpdate();
                }
            }
        }
    }

    private double getUserBalance(Connection con, int userId) throws SQLException {
        String sql = "SELECT balance FROM users WHERE user_id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble("balance");
            }
        }
        return 0;
    }

    private double getWalletBalance(Connection con, int userId) throws SQLException {
        String sql = "SELECT balance FROM wallet WHERE user_id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble("balance");
            }
        }
        return 0;
    }

    private int findUserId(Connection con, String identifier) throws SQLException {
        String query = "SELECT user_id FROM users WHERE upi_id = ? OR account_number = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, identifier);
            ps.setString(2, identifier);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("user_id");
            }
        }
        return -1;
    }

    public void transferWalletToUser(int senderId, String recId, double amount) {
        if (amount <= 0) {
            System.out.println("Amount must be greater than 0.");
            return;
        }

        Connection con = null;
        try {
            con = DBConnection.getConnection();
            con.setAutoCommit(false);

            ensureWallet(con, senderId);

            int receiverId = findUserId(con, recId);
            if (receiverId == -1) {
                System.out.println("Receiver not found.");
                con.rollback();
                return;
            }

            if (receiverId == senderId) {
                System.out.println("You cannot transfer to your own account.");
                con.rollback();
                return;
            }

            double walletBalance = getWalletBalance(con, senderId);
            if (walletBalance < amount) {
                System.out.println("Insufficient wallet balance.");
                con.rollback();
                return;
            }

            String deductWallet = "UPDATE wallet SET balance = balance - ? WHERE user_id = ?";
            try (PreparedStatement ps = con.prepareStatement(deductWallet)) {
                ps.setDouble(1, amount);
                ps.setInt(2, senderId);
                ps.executeUpdate();
            }

            String addUser = "UPDATE users SET balance = balance + ? WHERE user_id = ?";
            try (PreparedStatement ps = con.prepareStatement(addUser)) {
                ps.setDouble(1, amount);
                ps.setInt(2, receiverId);
                ps.executeUpdate();
            }

            String transactionQuery = "INSERT INTO transactions (sender_id, receiver_id, amount, transaction_type) VALUES (?, ?, ?, 'WALLET')";
            try (PreparedStatement ps = con.prepareStatement(transactionQuery)) {
                ps.setInt(1, senderId);
                ps.setInt(2, receiverId);
                ps.setDouble(3, amount);
                ps.executeUpdate();
            }

            con.commit();
            System.out.println("Transfer completed successfully.");

        } catch (Exception e) {
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
        } finally {
            if (con != null) {
                try {
                    con.setAutoCommit(true);
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

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
