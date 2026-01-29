package modules.Wallet;

import java.util.Scanner;

public class walletMenu {

    public static void showWalletMenu(int userId) {

        WalletDAO walletDAO = new WalletDAO();
        Scanner sc = new Scanner(System.in);

        walletDAO.createWallet(userId);

        while (true) {
            System.out.println("\n----- WALLET MODULE -----");
            System.out.println("1. Add Money");
            System.out.println("2. Check Balance");
            System.out.println("3. Exit Wallet");
            System.out.print("Enter choice: ");

            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Enter amount: ");
                    double amount = sc.nextDouble();
                    walletDAO.addMoney(userId, amount);
                    break;

                case 2:
                    double balance = walletDAO.getBalance(userId);
                    System.out.println("Current Balance: " + balance);
                    break;

                case 3:
                    System.out.println("Exiting wallet...");
                    return;

                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
}

