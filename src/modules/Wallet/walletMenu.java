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
            System.out.println("3. Transfer Money");
            System.out.println("4. Exit Wallet");
            System.out.print("Enter choice: ");

            // Menu validation
            if (!sc.hasNextInt()) {
                System.out.println("Invalid choice! Enter numbers only.");
                sc.next(); // clear invalid input
                continue;
            }

            int choice = sc.nextInt();

            switch (choice) {

                case 1:
                    double amount = 0;

                    // Amount validation loop
                    while (true) {
                        System.out.print("Enter amount to add: ");

                        if (!sc.hasNextDouble()) {
                            System.out.println("Invalid input! Enter numeric amount.");
                            sc.next();
                            continue;
                        }

                        amount = sc.nextDouble();

                        if (amount <= 0) {
                            System.out.println("Amount must be greater than 0.");
                        } else if (amount > 100000) {
                            System.out.println("Maximum limit is 1,00,000.");
                        } else {
                            break; // valid amount
                        }
                    }

                    walletDAO.addMoney(userId, amount);
                    break;

                case 2:
                    double balance = walletDAO.getBalance(userId);
                    System.out.printf("Current Balance: %.2f\n", balance);
                    break;

                case 3:
                    System.out.print("Enter receiver UPI ID or Account Number: ");
                    String receiverIdentifier = sc.next().trim();

                    double transferAmount = 0;
                    while (true) {
                        System.out.print("Enter amount to transfer: ");

                        if (!sc.hasNextDouble()) {
                            System.out.println("Invalid input! Enter numeric amount.");
                            sc.next();
                            continue;
                        }

                        transferAmount = sc.nextDouble();

                        if (transferAmount <= 0) {
                            System.out.println("Amount must be greater than 0.");
                        } else if (transferAmount > 10000) {
                            System.out.println("Maximum limit is 10000.");
                        } else {
                            break;
                        }
                    }

                    walletDAO.transferWalletToUser(userId, receiverIdentifier, transferAmount);
                    break;

                case 4:
                    System.out.println("Exiting wallet...");
                    return;

                default:
                    System.out.println("Invalid choice! Please select 1–4.");
            }
        }
    }
}
