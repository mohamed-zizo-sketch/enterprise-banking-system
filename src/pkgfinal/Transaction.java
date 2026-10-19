package pkgfinal;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents an audited ledger record for a financial transaction.
 * Tracks transaction ID, account number, operation type, amount, post-transaction balance,
 * timestamp, and reference note.
 */
public class Transaction {
    public enum Type {
        DEPOSIT, WITHDRAWAL, TRANSFER_IN, TRANSFER_OUT
    }

    private int transactionId;
    private int accountNumber;
    private Type type;
    private double amount;
    private double balanceAfter;
    private String timestamp;
    private String description;

    public Transaction(int transactionId, int accountNumber, Type type, double amount, double balanceAfter, String description) {
        this.transactionId = transactionId;
        this.accountNumber = accountNumber;
        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.description = description;
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public Transaction(int transactionId, int accountNumber, Type type, double amount, double balanceAfter, String timestamp, String description) {
        this.transactionId = transactionId;
        this.accountNumber = accountNumber;
        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.timestamp = timestamp;
        this.description = description;
    }

    public int getTransactionId() { return transactionId; }
    public int getAccountNumber() { return accountNumber; }
    public Type getType() { return type; }
    public double getAmount() { return amount; }
    public double getBalanceAfter() { return balanceAfter; }
    public String getTimestamp() { return timestamp; }
    public String getDescription() { return description; }

    public String displayInfo() {
        return String.format("[%s] %-12s | Acc: %-6d | Amount: $%-9.2f | Balance: $%-9.2f | %s",
                timestamp, type, accountNumber, amount, balanceAfter, description);
    }
}
