package Admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class Admin {

    private final Scanner sc = new Scanner(System.in);

    // ✅ Admin Login
    public boolean login() {
        System.out.println("=========== Admin Login ==============");
        System.out.print("Enter Username: ");
        String username = sc.next();

        System.out.print("Enter Password: ");
        String password = sc.next();

        String query = "SELECT user_id FROM users WHERE username=? AND password=? AND role='ADMIN'";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                System.out.println("Admin Login Successful!");
                return true;
            }

        } catch (Exception e) {
            System.out.println("Admin Login Error!");
            e.printStackTrace();
        }

        System.out.println("Invalid Credentials!");
        return false;
    }

    // ✅ View All Users
    public void viewUsers() {
        System.out.println("=========== All Users ===========");
        String query = "SELECT user_id, full_name, role, balance FROM users WHERE role='USER'";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.println("--------------------------------");
                System.out.println("User ID : " + rs.getInt("user_id"));
                System.out.println("Name    : " + rs.getString("full_name"));
                System.out.println("Balance : " + rs.getDouble("balance"));
            }

            if (!found) {
                System.out.println("No Users Found!");
            }

        } catch (Exception e) {
            System.out.println("Error in View Users!");
            e.printStackTrace();
        }
    }

    // ✅ View All Transactions
    public void viewAllTransactions() {
        System.out.println("============= Transaction History ===========");

        String query =
                "SELECT t.transaction_id, t.amount, t.created_at, " +
                        "sender.username AS sender_name, receiver.username AS receiver_name " +
                        "FROM transactions t " +
                        "JOIN users sender ON t.sender_id = sender.user_id " +
                        "JOIN users receiver ON t.receiver_id = receiver.user_id " +
                        "ORDER BY t.created_at DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.println("--------------------------------");
                System.out.println("Transaction ID: " + rs.getInt("transaction_id"));
                System.out.println("From: " + rs.getString("sender_name"));
                System.out.println("To: " + rs.getString("receiver_name"));
                System.out.println("Amount: " + rs.getDouble("amount"));
                System.out.println("Date: " + rs.getString("created_at"));
            }

            if (!found) {
                System.out.println("No Transactions Found!");
            }

        } catch (Exception e) {
            System.out.println("Error in Transactions!");
            e.printStackTrace();
        }
    }

    // ✅ Remove User
    public void removeUser() {
        System.out.println("=========== Remove User ==============");
        System.out.print("Enter Username: ");
        String username = sc.next();

        String query = "DELETE FROM users WHERE username=? AND role='USER'";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setString(1, username);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                System.out.println("User Removed Successfully");
            } else {
                System.out.println("User Not Found!");
            }

        } catch (Exception e) {
            System.out.println("Remove User Error");
            e.printStackTrace();
        }
    }

    // ✅ Admin Menu Main Method
    public static void main(String[] args) {
        Admin admin = new Admin();
        Scanner sc = new Scanner(System.in);

        if (admin.login()) {
            while (true) {
                System.out.println("\n--- Admin Menu ---");
                System.out.println("1. View All Users");
                System.out.println("2. View All Transactions");
                System.out.println("3. Remove User");
                System.out.println("4. Exit");
                System.out.print("Enter choice: ");

                int choice = sc.nextInt();

                // 🔹 Old-style switch for compatibility
                switch (choice) {
                    case 1:
                        admin.viewUsers();
                        break;
                    case 2:
                        admin.viewAllTransactions();
                        break;
                    case 3:
                        admin.removeUser();
                        break;
                    case 4:
                        System.out.println("Exiting");
                        return;
                    default:
                        System.out.println("Invalid choice");
                }
            }
        } else {
            System.out.println("Exiting...");
        }
    }
}
