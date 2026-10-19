package pkgfinal;

/**
 * Models an interest-bearing Savings Account.
 * Supports deposits, withdrawals, monthly interest accrual, and yield calculation.
 */
public class SavingsAccount extends Account {

    private double interestRate; // e.g. 0.05 for 5% annual interest

    public SavingsAccount(int accNum, double balance, double interestRate) {
        super(accNum, balance);
        this.interestRate = interestRate;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    /**
     * Calculates interest accrued over a single month.
     */
    public double calculateMonthlyInterest() {
        return getBalance() * (interestRate / 12.0);
    }

    /**
     * Deposits accrued monthly interest into the account.
     */
    public void applyMonthlyInterest() {
        deposit(calculateMonthlyInterest());
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
        if (amount > getBalance()) {
            throw new IllegalArgumentException("Insufficient balance! Available: $" + String.format("%.2f", getBalance()) +
                    ", attempted: $" + String.format("%.2f", amount));
        }
        setBalance(getBalance() - amount);
    }

    @Override
    public String displayInfo() {
        return super.displayInfo() + " | Type: Savings (Interest: " + String.format("%.1f%%", interestRate * 100) + ")";
    }
}
