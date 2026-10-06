import java.util.List;
import java.util.Scanner;
 
/**
 * The ATM machine: handles login and the main menu.
 */
public class ATM {
 
    private static final int MAX_LOGIN_ATTEMPTS = 3;
 
    private final Bank bank;
    private final Scanner scanner = new Scanner(System.in);
 
    public ATM(Bank bank) {
        this.bank = bank;
    }
 
    public void start() {
        System.out.println("=====================================");
        System.out.println("   WELCOME TO " + bank.getName().toUpperCase() + " ATM");
        System.out.println("=====================================");
 
        Account account = login();
        if (account == null) {
            System.out.println("\nToo many incorrect attempts. Access denied.");
            return;
        }
 
        System.out.println("\nLogin successful. Welcome, " + account.getHolderName() + "!");
        runMenu(account);
        System.out.println("\nThank you for using our ATM. Goodbye!");
    }
 
    private Account login() {
        for (int attempt = 1; attempt <= MAX_LOGIN_ATTEMPTS; attempt++) {
            System.out.print("\nEnter User ID: ");
            String userId = scanner.nextLine().trim();
            System.out.print("Enter PIN: ");
            String pin = scanner.nextLine().trim();
 
            Account account = bank.authenticate(userId, pin);
            if (account != null) {
                return account;
            }
            int left = MAX_LOGIN_ATTEMPTS - attempt;
            if (left > 0) {
                System.out.println("Invalid User ID or PIN. Attempts left: " + left);
            }
        }
        return null;
    }
 
    private void runMenu(Account account) {
        boolean running = true;
        while (running) {
            System.out.println("\n----------- MAIN MENU -----------");
            System.out.println("1. Transaction History");
            System.out.println("2. Withdraw");
            System.out.println("3. Deposit");
            System.out.println("4. Transfer");
            System.out.println("5. Quit");
            System.out.println("---------------------------------");
            int choice = readInt("Choose an option (1-5): ");
 
            switch (choice) {
                case 1:
                    showHistory(account);
                    break;
                case 2:
                    withdraw(account);
                    break;
                case 3:
                    deposit(account);
                    break;
                case 4:
                    transfer(account);
                    break;
                case 5:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Please choose 1 to 5.");
            }
        }
    }
 
    private void showHistory(Account account) {
        List<Transaction> history = account.getHistory();
        System.out.println("\n======= TRANSACTION HISTORY =======");
        if (history.isEmpty()) {
            System.out.println("No transactions in this session yet.");
        } else {
            for (Transaction t : history) {
                System.out.println(t);
            }
        }
        System.out.printf("Current balance: %.2f%n", account.getBalance());
    }
 
    private void withdraw(Account account) {
        double amount = readAmount("Enter amount to withdraw: ");
        if (account.withdraw(amount)) {
            System.out.printf("Please collect your cash. New balance: %.2f%n", account.getBalance());
        } else {
            System.out.println("Insufficient Funds");
        }
    }
 
    private void deposit(Account account) {
        double amount = readAmount("Enter amount to deposit: ");
        account.deposit(amount);
        System.out.printf("Deposit successful. New balance: %.2f%n", account.getBalance());
    }
 
    private void transfer(Account account) {
        System.out.print("Enter recipient User ID: ");
        String toUserId = scanner.nextLine().trim();
        double amount = readAmount("Enter amount to transfer: ");
 
        String result = bank.transfer(account, toUserId, amount);
        if (result.equals("OK")) {
            System.out.printf("Transferred %.2f to %s. New balance: %.2f%n",
                    amount, toUserId, account.getBalance());
        } else {
            System.out.println(result);
        }
    }
 
    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }
 
    private double readAmount(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                double amount = Double.parseDouble(scanner.nextLine().trim());
                if (amount <= 0) {
                    System.out.println("Amount must be greater than zero.");
                } else {
                    return amount;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid amount. Please enter a number.");
            }
        }
    }
}