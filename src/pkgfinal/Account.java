package pkgfinal;

/**
 * Abstract base class representing a bank account.
 * Implements fundamental account operations and attributes.
 */
public abstract class Account implements AccountOperations {

    protected int accountNumber;
    protected double balance;

    public Account(int accountNumber, double balance) {
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    /**
     * Default base implementation for displaying account details.
     */
    public String displayInfo() {
        return "Account #" + accountNumber + " | Balance: $" + String.format("%.2f", balance);
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
