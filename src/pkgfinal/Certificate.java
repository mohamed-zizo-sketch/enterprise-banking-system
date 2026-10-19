package pkgfinal;

/**
 * Models a Bank Certificate of Deposit (CD) investment product.
 * Provides guaranteed annual yields, monthly dividend returns, and maturity valuation.
 */
public class Certificate {
    private int certificateId;
    private int customerId;
    private double principalAmount;
    private double annualInterestRate;
    private int durationMonths;

    public Certificate(int certificateId, int customerId, double principalAmount, double annualInterestRate, int durationMonths) {
        this.certificateId = certificateId;
        this.customerId = customerId;
        this.principalAmount = principalAmount;
        this.annualInterestRate = annualInterestRate;
        this.durationMonths = durationMonths;
    }

    public int getCertificateId() { return certificateId; }
    public int getCustomerId() { return customerId; }
    public double getPrincipalAmount() { return principalAmount; }
    public double getAnnualInterestRate() { return annualInterestRate; }
    public int getDurationMonths() { return durationMonths; }

    public double getMonthlyReturn() {
        return (principalAmount * annualInterestRate) / 12.0;
    }

    public double getMaturityValue() {
        return principalAmount + (principalAmount * annualInterestRate * (durationMonths / 12.0));
    }

    public String displayInfo() {
        return "Certificate #" + certificateId + " | Customer ID: " + customerId +
               " | Principal: $" + String.format("%.2f", principalAmount) +
               " | Yield: " + String.format("%.1f%%", annualInterestRate * 100) +
               " | Duration: " + durationMonths + " months" +
               " | Maturity Value: $" + String.format("%.2f", getMaturityValue());
    }
}
