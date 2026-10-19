package pkgfinal;

import java.util.ArrayList;

/**
 * Top-level domain model representing the commercial banking enterprise.
 */
public class Bank {
    private String bankName;
    private ArrayList<Customer> customers;
    private ArrayList<Branch> branches;

    public Bank() {
        this("Global Horizon Enterprise Bank");
    }

    public Bank(String bankName) {
        this.bankName = bankName;
        this.customers = new ArrayList<>();
        this.branches = new ArrayList<>();
    }

    public String getBankName() {
        return bankName;
    }

    public void addCustomer(Customer c) {
        customers.add(c);
    }

    public ArrayList<Customer> getCustomers() {
        return customers;
    }

    public Customer findCustomer(int id) {
        for (Customer c : customers) {
            if (c.getCustomerId() == id) {
                return c;
            }
        }
        return null;
    }

    public void addBranch(Branch b) {
        branches.add(b);
    }

    public ArrayList<Branch> getBranches() {
        return branches;
    }
}