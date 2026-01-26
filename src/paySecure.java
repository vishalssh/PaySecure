import java.util.Scanner;

import modules.User.User;
import modules.User.userlogin;

public class paySecure {
    private Scanner scanner = new Scanner(System.in);
    private userlogin loggedInUser = null;

    public static void main(String[] args) throws Exception {
        System.out.println("+++++++++++++++ Project PaySecure ++++++++++++++++");
        paySecure app = new paySecure();
        app.showAuthMenu();
    }


    void showAuthMenu() {
        while (true) {
            System.out.println("===========================================");
            System.out.println("      Welcome to PaySecure System");
            System.out.println("===========================================");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
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
                case 3:
                    System.out.println("Thank you for using PaySecure. Goodbye!");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid option! Please try again.");
            }
        }
    }

    void registerUser() {
        User user = new User();
        user.registerUser();
        user.showUser();
    }

    void loginUser() {
        loggedInUser = new userlogin();
        if (loggedInUser.login()) {
            showUserMenu();
        }
    }

    void showUserMenu() {
        while (true) {
            System.out.println("===========================================");
            System.out.println("      PaySecure - Main Menu");
            System.out.println("===========================================");
            System.out.println("1. View Profile");
            System.out.println("2. Transfer Money");
            System.out.println("3. Check Balance");
            System.out.println("4. Transaction History");
            System.out.println("5. Logout");
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
                    System.out.println("Transfer Money - Coming soon!");
                    break;
                case 3:
                    System.out.println("Check Balance - Coming soon!");
                    break;
                case 4:
                    System.out.println("Transaction History - Coming soon!");
                    break;
                case 5:
                    System.out.println("Logged out successfully!");
                    loggedInUser = null;
                    return;
                default:
                    System.out.println("Invalid option! Please try again.");
            }
        }
    }
}
