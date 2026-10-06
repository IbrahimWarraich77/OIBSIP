import java.util.HashMap;
import java.util.Map;
 
/**
 * Holds all accounts and handles authentication and transfers.
 */
public class Bank {
 
    private final String name;
    private final Map<String, Account> accounts = new HashMap<>();
 
    public Bank(String name) {
        this.name = name;
    }
 
    public String getName() {
        return name;
    }
 
    public void addAccount(Account account) {
        accounts.put(account.getUserId(), account);
    }
 
    public Account findAccount(String userId) {
        return accounts.get(userId);
    }
 
    /** Returns the account if ID and PIN match, otherwise null. */
    public Account authenticate(String userId, String pin) {
        Account account = accounts.get(userId);
        if (account != null && account.checkPin(pin)) {
            return account;
        }
        return null;
    }
 
    /**
     * Moves money between two accounts.
     * Returns a message describing the result ("OK" on success).
     */
    public String transfer(Account from, String toUserId, double amount) {
        Account to = accounts.get(toUserId);
        if (to == null) {
            return "Recipient account not found.";
        }
        if (to == from) {
            return "You cannot transfer to your own account.";
        }
        if (!from.hasSufficientFunds(amount)) {
            return "Insufficient Funds";
        }
        from.transferOut(amount, toUserId);
        to.transferIn(amount, from.getUserId());
        return "OK";
    }
}