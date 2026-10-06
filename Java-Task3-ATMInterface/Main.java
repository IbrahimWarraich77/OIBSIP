/**
 * Oasis Infobyte SIP - Java Development
 * Task 3: ATM Interface
 *
 * Demo accounts:
 *   User ID: user1  PIN: 1234  (balance 5000)
 *   User ID: user2  PIN: 4321  (balance 3000)
 */
public class Main {
 
    public static void main(String[] args) {
        Bank bank = new Bank("OIBSIP Bank");
        bank.addAccount(new Account("user1", "Ali Khan", "1234", 5000.00));
        bank.addAccount(new Account("user2", "Sara Ahmed", "4321", 3000.00));
 
        ATM atm = new ATM(bank);
        atm.start();
    }
}
 
