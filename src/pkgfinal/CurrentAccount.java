package pkgfinal;

/**
 * Models a Commercial Current / Checking Account with an approved Overdraft Limit.
 * Allows withdrawals exceeding the positive balance up to the configured overdraft buffer.
 */
public class CurrentAccount extends Account {
    private double overdraftLimit;

    public CurrentAccount(int accNum, double balance) {
        this(accNum, balance, 1000.0); // Default $1,000 overdraft facility
    }

    public CurrentAccount(int accNum, double balance, double overdraftLimit) {
        super(accNum, balance);
        this.overdraftLimit = Math.max(0.0, overdraftLimit);
    }

    public double getOverdraftLimit() {
        return overdraftLimit;
    }

    public void setOverdraftLimit(double overdraftLimit) {
        this.overdraftLimit = Math.max(0.0, overdraftLimit);
    }

    public double getTotalAvailableFunds() {
        return getBalance() + overdraftLimit;
    }

    @Override
    public void deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be strictly positive.");
        }
        setBalance(getBalance() + amount);
    }

    @Override
    public void withdraw(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be strictly positive.");
        }
        double available = getTotalAvailableFunds();
        if (amount > available) {
            throw new IllegalArgumentException("Overdraft limit exceeded! Maximum accessible funds: $" +
                    String.format("%.2f", available) + ", attempted: $" + String.format("%.2f", amount));
        }
        // Balance may dip below zero down to -overdraftLimit
        this.balance -= amount;
    }

    @Override
    public String displayInfo() {
        return super.displayInfo() + " | Type: Current (Overdraft Limit: $" + String.format("%.2f", overdraftLimit) + ")";
    }
}