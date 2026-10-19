package pkgfinal;

import java.util.ArrayList;

/**
 * Models a registered banking client.
 * Owns multiple deposit/checking accounts and certificates of deposit.
 */
public class Customer extends Person {

    private int customerId;
    private ArrayList<Account> accounts;

    public Customer(int customerId, String name, String nationalId, String phone) {
        super(name, nationalId, phone);
        this.customerId = customerId;
        this.accounts = new ArrayList<>();
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public ArrayList<Account> getAccounts() {
        return accounts;
    }

    public void addAccount(Account acc) {
        accounts.add(acc);
    }

    public double getTotalBalance() {
        double sum = 0;
        for (Account a : accounts) {
            sum += a.getBalance();
        }
        return sum;
    }

    public String displayInfo() {
        return "Customer #" + customerId + " | Name: " + name + " | National ID: " + nationalId + " | Phone: " + phone;
    }
}
