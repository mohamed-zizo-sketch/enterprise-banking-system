package pkgfinal;

import java.util.ArrayList;

/**
 * Models a physical commercial bank branch with operating status,
 * management details, contact coordinates, and assigned personnel.
 */
public class Branch {
    private String branchCode;
    private String branchName;
    private String address;
    private String city;
    private String phoneNumber;
    private String managerName;
    private boolean isOpen;
    private ArrayList<Employee> employees;

    public Branch(String branchCode, String branchName, String address, String city, String phoneNumber, String managerName, boolean isOpen) {
        this.branchCode = branchCode;
        this.branchName = branchName;
        this.address = address;
        this.city = city;
        this.phoneNumber = phoneNumber;
        this.managerName = managerName;
        this.isOpen = isOpen;
        this.employees = new ArrayList<>();
    }

    public String getBranchCode() { return branchCode; }
    public String getBranchName() { return branchName; }
    public String getAddress() { return address; }
    public String getCity() { return city; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getManagerName() { return managerName; }
    public boolean isOpen() { return isOpen; }
    public ArrayList<Employee> getEmployees() { return employees; }

    public void addEmployee(Employee e) {
        employees.add(e);
    }

    public String displayInfo() {
        return "Branch " + branchCode + " (" + branchName + ") - " + city +
               " | Manager: " + managerName + " | Tel: " + phoneNumber +
               " | Employees: " + employees.size() + " | Status: " + (isOpen ? "Open" : "Closed");
    }
}
