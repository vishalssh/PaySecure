import java.sql.SQLException;
import java.util.Scanner;

import modules.User.User;
import modules.User.userlogin;
// import modules.Transaction.TransferService;
// import modules.Admin.Admin;
// import modules.Wallet.walletMenu;

public class paySecure {
    Scanner scanner = new Scanner(System.in);
    userlogin loggedInUser = null;
    // Admin loggedInAdmin = null;

    public static void main(String[] args) throws Exception {
        System.out.println("+++++++++++++++ Project PaySecure ++++++++++++++++");
        paySecure app = new paySecure();
        app.showAuthMenu();
    }

    public void showAuthMenu() throws SQLException {
        while (true) {
            System.out.println("===========================================");
            System.out.println("      Welcome to PaySecure System");
            System.out.println("===========================================");
            System.out.println("1. Register");
            System.out.println("2. User Login");
            System.out.println("3. Admin Login");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");

            int choice = 0;
            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number.");
                continue;
            }

            switch (choice) {
                case 1:
                    registerUser();
                    break;
                case 2:
                    loginUser();
                    break;
                // case 3:
                //     loginAdmin();
                //     break;
                case 4:
                    System.out.println("Thank you for using PaySecure. Goodbye!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid option! Please try again.");
            }
        }
    }

    public void registerUser() throws SQLException {
        User user = new User();
        user.registerUser();
        user.showUser();
    }

    public void loginUser() throws SQLException {
        loggedInUser = new userlogin();
        if (loggedInUser.login()) {
            showUserMenu();
        }
    }

    // public void loginAdmin() throws SQLException {
    //     loggedInAdmin = new Admin();
    //     if (loggedInAdmin.login()) {
    //         showAdminMenu();
    //     }
    // }

    public void wallet() {
        // walletMenu.showWalletMenu(loggedInUser.userId);
    }

    // public void transferMoney() throws SQLException {
    //     TransferService transferService = new TransferService();
    //     transferService.transferMoney(loggedInUser.userId);
    // }

    // public void showHistory() throws SQLException {
    //     TransferService transferService = new TransferService();
    //     transferService.showHistory(loggedInUser.userId);
    // }

    public void showUserMenu() throws SQLException {
        while (true) {
            System.out.println("===========================================");
            System.out.println("      PaySecure - Main Menu");
            System.out.println("===========================================");
            System.out.println("1. View Profile");
            System.out.println("2. Check Balance");
            System.out.println("3. Add Balance");
            System.out.println("4. Wallet");
            System.out.println("5. Transfer Money");
            System.out.println("6. Transaction History");
            System.out.println("7. Logout");
            System.out.print("Choose an option: ");

            int choice = 0;
            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a number.");
                continue;
            }

            switch (choice) {
                case 1:
                    loggedInUser.showProfile();
                    break;
                case 2:
                    loggedInUser.checkBalance();
                    break;
                case 3:
                    loggedInUser.addBalance();
                    break;
                case 4:
                    wallet();
                    break;
                // case 5:
                //     transferMoney();
                //     break;
                // case 6:
                //     showHistory();
                //     break;
                case 7:
                    System.out.println("Logged out successfully!");
                    return;
                default:
                    System.out.println("Invalid option! Please try again.");
            }
        }
    }

    // public void showAdminMenu() throws SQLException {
    //     while (true) {
    //         System.out.println("===========================================");
    //         System.out.println("      PaySecure - Admin Menu");
    //         System.out.println("===========================================");
    //         System.out.println("1. View Users");
    //         System.out.println("2. Transaction History");
    //         System.out.println("3. Remove User");
    //         System.out.println("4. Logout");
    //         System.out.print("Choose an option: ");

    //         int choice = 0;
    //         try {
    //             choice = Integer.parseInt(scanner.nextLine().trim());
    //         } catch (NumberFormatException e) {
    //             System.out.println("Invalid input! Please enter a number.");
    //             continue;
    //         }

    //         switch (choice) {
    //             case 1:
    //                 loggedInAdmin.viewUsers();
    //                 break;
    //             case 2:
    //                 loggedInAdmin.viewAllTransactions();
    //                 break;
    //             case 3:
    //                 loggedInAdmin.removeUser();
    //                 break;
    //             case 4:
    //                 System.out.println("Admin logged out successfully!");
    //                 return;
    //             default:
    //                 System.out.println("Invalid option! Please try again.");
    //         }
    //     }
    // }
}
