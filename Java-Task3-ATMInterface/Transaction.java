
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
 
/**
 * Represents a single transaction (withdraw, deposit, transfer in/out).
 */
public class Transaction {
 
    private static final DateTimeFormatter FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
 
    private final String type;
    private final double amount;
    private final String details;
    private final double balanceAfter;
    private final LocalDateTime timestamp;
 
    public Transaction(String type, double amount, String details, double balanceAfter) {
        this.type = type;
        this.amount = amount;
        this.details = details;
        this.balanceAfter = balanceAfter;
        this.timestamp = LocalDateTime.now();
    }
 
    public String getType() {
        return type;
    }
 
    public double getAmount() {
        return amount;
    }
 
    public String getDetails() {
        return details;
    }
 
    public double getBalanceAfter() {
        return balanceAfter;
    }
 
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
 
    @Override
    public String toString() {
        return String.format("%s | %-13s | %10.2f | Balance: %10.2f | %s",
                timestamp.format(FORMAT), type, amount, balanceAfter, details);
    }
}
 