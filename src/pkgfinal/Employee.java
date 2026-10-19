package pkgfinal;

/**
 * Models bank personnel and staff members.
 */
public class Employee extends Person {
    protected int employeeId;
    protected String role;
    protected double salary;
    protected String branchCode;

    public Employee(int employeeId, String name, String nationalId, String phone, String role, double salary, String branchCode) {
        super(name, nationalId, phone);
        this.employeeId = employeeId;
        this.role = role;
        this.salary = salary;
        this.branchCode = branchCode;
    }

    public int getEmployeeId() { return employeeId; }
    public String getRole() { return role; }
    public double getSalary() { return salary; }
    public String getBranchCode() { return branchCode; }

    public String displayInfo() {
        return "Staff #" + employeeId + " | " + name + " | Role: " + role +
               " | Branch: " + branchCode + " | Salary: $" + String.format("%.2f", salary);
    }
}
