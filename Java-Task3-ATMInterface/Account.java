
import java.util.ArrayList;
import java.util.List;
 
/**
 * Represents a bank account with a PIN, balance and transaction history.
 */
public class Account {
 
    private final String userId;
    private final String holderName;
    private String pin;
    private double balance;
    private final List<Transaction> history = new ArrayList<>();
 
    public Account(String userId, String holderName, String pin, double openingBalance) {
        this.userId = userId;
        this.holderName = holderName;
        this.pin = pin;
        this.balance = openingBalance;
    }
 
    public String getUserId() {
        return userId;
    }
 
    public String getHolderName() {
        return holderName;
    }
 
    public double getBalance() {
        return balance;
    }
 
    public List<Transaction> getHistory() {
        return history;
    }
 
    public boolean checkPin(String enteredPin) {
        return pin.equals(enteredPin);
    }
 
    public boolean hasSufficientFunds(double amount) {
        return balance >= amount;
    }
 
    public void deposit(double amount) {
        balance += amount;
        history.add(new Transaction("DEPOSIT", amount, "Cash deposit", balance));
    }
 
    public boolean withdraw(double amount) {
        if (!hasSufficientFunds(amount)) {
            return false;
        }
        balance -= amount;
        history.add(new Transaction("WITHDRAW", amount, "Cash withdrawal", balance));
        return true;
    }
 
    /** Called on the sender's account. */
    public boolean transferOut(double amount, String toUserId) {
        if (!hasSufficientFunds(amount)) {
            return false;
        }
        balance -= amount;
        history.add(new Transaction("TRANSFER OUT", amount, "To account " + toUserId, balance));
        return true;
    }
 
    /** Called on the receiver's account. */
    public void transferIn(double amount, String fromUserId) {
        balance += amount;
        history.add(new Transaction("TRANSFER IN", amount, "From account " + fromUserId, balance));
    }
}