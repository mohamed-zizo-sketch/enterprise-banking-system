package pkgfinal;

import java.sql.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Enterprise Data Access Layer.
 * Operates against Oracle Database XE when online, with automatic seamless
 * failover to a thread-safe embedded repository when running standalone.
 */
public class DBManager {

    // --- In-Memory Fallback Repository (Thread-Safe) ---
    private static final Map<Integer, Customer> mockCustomers = new ConcurrentHashMap<>();
    private static final Map<Integer, Account> mockAccounts = new ConcurrentHashMap<>();
    private static final Map<Integer, Integer> accountToCustomer = new ConcurrentHashMap<>();
    private static final List<Transaction> mockTransactions = Collections.synchronizedList(new ArrayList<>());
    private static final List<Certificate> mockCertificates = Collections.synchronizedList(new ArrayList<>());
    private static final List<Branch> mockBranches = new ArrayList<>();
    private static final AtomicInteger transactionIdSeq = new AtomicInteger(1001);
    private static final AtomicInteger certificateIdSeq = new AtomicInteger(501);

    static {
        // Seed default branches
        Branch b1 = new Branch("B001", "Smouha Branch", "10 Victor Emmanuel St", "Alexandria", "03-4200000", "Eng. Walid", true);
        b1.addEmployee(new Employee(101, "Karim Nabil", "29801010101234", "01001112222", "Branch Manager", 35000, "B001"));
        b1.addEmployee(new Employee(102, "Nour El-Din", "29902020205678", "01112223333", "Senior Teller", 18000, "B001"));

        Branch b2 = new Branch("B002", "Maadi Branch", "Road 9, Degla", "Cairo", "02-2350000", "Eng. Mohamed Zizo", true);
        b2.addEmployee(new Employee(201, "Youssef Tarek", "29505050509988", "01223334444", "Operations Supervisor", 38000, "B002"));

        Branch b3 = new Branch("B003", "El Raml Branch", "Saad Zaghloul Square", "Alexandria", "03-4800000", "Hany Mostafa", true);

        mockBranches.add(b1);
        mockBranches.add(b2);
        mockBranches.add(b3);

        // Seed sample demo data
        Customer c1 = new Customer(1, "Mohamed Zizo", "29901010100111", "01090000001");
        Customer c2 = new Customer(2, "Ahmed Hassan", "29805050500222", "01120000002");
        Customer c3 = new Customer(3, "Sarah Mahmoud", "29709090900333", "01230000003");

        mockCustomers.put(1, c1);
        mockCustomers.put(2, c2);
        mockCustomers.put(3, c3);

        SavingsAccount a1 = new SavingsAccount(101, 25000.0, 0.08);
        CurrentAccount a2 = new CurrentAccount(102, 12000.0, 5000.0);
        SavingsAccount a3 = new SavingsAccount(103, 50000.0, 0.10);

        mockAccounts.put(101, a1);
        mockAccounts.put(102, a2);
        mockAccounts.put(103, a3);

        accountToCustomer.put(101, 1);
        accountToCustomer.put(102, 1);
        accountToCustomer.put(103, 2);

        c1.addAccount(a1);
        c1.addAccount(a2);
        c2.addAccount(a3);

        mockTransactions.add(new Transaction(1001, 101, Transaction.Type.DEPOSIT, 25000.0, 25000.0, "Initial Account Opening Deposit"));
        mockTransactions.add(new Transaction(1002, 102, Transaction.Type.DEPOSIT, 12000.0, 12000.0, "Initial Current Account Deposit"));
        mockTransactions.add(new Transaction(1003, 103, Transaction.Type.DEPOSIT, 50000.0, 50000.0, "High-Yield Savings Deposit"));

        mockCertificates.add(new Certificate(501, 1, 100000.0, 0.15, 36));
    }

    public static boolean isUsingOracle() {
        return dbConn.isOracleAvailable();
    }

    // --- Customer Operations ---
    public static void saveCustomer(Customer c) throws SQLException {
        if (isUsingOracle()) {
            String sql = "INSERT INTO CUSTOMER (CUSTOMERID, NAME, NATIONALID, PHONE) VALUES (?, ?, ?, ?)";
            try (Connection conn = dbConn.DBConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, c.getCustomerId());
                pstmt.setString(2, c.getName());
                pstmt.setString(3, c.getNationalId());
                pstmt.setString(4, c.getPhone());
                pstmt.executeUpdate();
            }
        }
        mockCustomers.put(c.getCustomerId(), c);
    }

    public static ArrayList<Customer> getAllCustomers() {
        if (isUsingOracle()) {
            ArrayList<Customer> list = new ArrayList<>();
            String sql = "SELECT * FROM CUSTOMER ORDER BY CUSTOMERID";
            try (Connection conn = dbConn.DBConnection();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                    Customer c = new Customer(
                            rs.getInt("CUSTOMERID"),
                            rs.getString("NAME"),
                            rs.getString("NATIONALID"),
                            rs.getString("PHONE")
                    );
                    list.add(c);
                }
                return list;
            } catch (SQLException e) {
                System.err.println("Oracle fallback to in-memory: " + e.getMessage());
            }
        }
        return new ArrayList<>(mockCustomers.values());
    }

    // --- Account Operations ---
    public static void saveAccount(int accNum, double balance, String type, int custId) throws SQLException {
        if (isUsingOracle()) {
            String sql = "INSERT INTO ACCOUNT (ACCOUNTNUMBER, BALANCE, TYPE, CUSTOMERID) VALUES (?, ?, ?, ?)";
            try (Connection conn = dbConn.DBConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, accNum);
                pstmt.setDouble(2, balance);
                pstmt.setString(3, type);
                pstmt.setInt(4, custId);
                pstmt.executeUpdate();
            } catch (SQLException e) {
                if (e.getErrorCode() == 2291) {
                    throw new SQLException("Cannot create account: Customer ID " + custId + " does not exist in database.");
                }
                throw e;
            }
        }

        // Keep local store in sync
        Customer customer = mockCustomers.get(custId);
        Account acc;
        if ("Current".equalsIgnoreCase(type)) {
            acc = new CurrentAccount(accNum, balance, 1000.0);
        } else {
            acc = new SavingsAccount(accNum, balance, 0.05);
        }
        mockAccounts.put(accNum, acc);
        accountToCustomer.put(accNum, custId);
        if (customer != null) {
            customer.addAccount(acc);
        }
        recordTransaction(accNum, Transaction.Type.DEPOSIT, balance, balance, "Account Registration Deposit");
    }

    public static double getBalance(int accNum) {
        if (isUsingOracle()) {
            String sql = "SELECT BALANCE FROM ACCOUNT WHERE ACCOUNTNUMBER = ?";
            try (Connection conn = dbConn.DBConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, accNum);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    return rs.getDouble("BALANCE");
                } else {
                    return -1.0;
                }
            } catch (SQLException e) {
                System.err.println("Oracle error, checking fallback: " + e.getMessage());
            }
        }
        Account a = mockAccounts.get(accNum);
        return (a != null) ? a.getBalance() : -1.0;
    }

    public static Account getAccount(int accNum) {
        if (mockAccounts.containsKey(accNum)) {
            return mockAccounts.get(accNum);
        }
        // Fallback for Oracle if not in map
        double bal = getBalance(accNum);
        if (bal != -1.0) {
            SavingsAccount sa = new SavingsAccount(accNum, bal, 0.05);
            mockAccounts.put(accNum, sa);
            return sa;
        }
        return null;
    }

    public static void updateBalance(int accNum, double newBalance) {
        if (isUsingOracle()) {
            String sql = "UPDATE ACCOUNT SET BALANCE = ? WHERE ACCOUNTNUMBER = ?";
            try (Connection conn = dbConn.DBConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setDouble(1, newBalance);
                pstmt.setInt(2, accNum);
                pstmt.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        Account a = mockAccounts.get(accNum);
        if (a != null) {
            a.setBalance(newBalance);
        }
    }

    public static ArrayList<Account> getAllAccounts() {
        return new ArrayList<>(mockAccounts.values());
    }

    // --- Fund Transfer Operation ---
    public static void transferFunds(int fromAccNum, int toAccNum, double amount) throws SQLException {
        if (fromAccNum == toAccNum) {
            throw new IllegalArgumentException("Source and destination accounts cannot be identical.");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Transfer amount must be strictly positive.");
        }

        Account source = getAccount(fromAccNum);
        Account dest = getAccount(toAccNum);

        if (source == null) {
            throw new IllegalArgumentException("Source Account #" + fromAccNum + " not found.");
        }
        if (dest == null) {
            throw new IllegalArgumentException("Destination Account #" + toAccNum + " not found.");
        }

        // Execute withdrawal from source (validates overdraft/balance)
        source.withdraw(amount);
        dest.deposit(amount);

        // Update database
        updateBalance(fromAccNum, source.getBalance());
        updateBalance(toAccNum, dest.getBalance());

        // Audit logs
        recordTransaction(fromAccNum, Transaction.Type.TRANSFER_OUT, amount, source.getBalance(),
                "Transfer to Account #" + toAccNum);
        recordTransaction(toAccNum, Transaction.Type.TRANSFER_IN, amount, dest.getBalance(),
                "Transfer from Account #" + fromAccNum);
    }

    // --- Transactions & Audit Trail ---
    public static void recordTransaction(int accNum, Transaction.Type type, double amount, double balanceAfter, String desc) {
        Transaction tx = new Transaction(transactionIdSeq.getAndIncrement(), accNum, type, amount, balanceAfter, desc);
        mockTransactions.add(0, tx); // prepend for chronological order
    }

    public static List<Transaction> getAllTransactions() {
        return new ArrayList<>(mockTransactions);
    }

    // --- Certificate Operations ---
    public static void saveCertificate(int custId, double amount, double rate, int months) {
        Certificate c = new Certificate(certificateIdSeq.getAndIncrement(), custId, amount, rate, months);
        mockCertificates.add(c);
    }

    public static List<Certificate> getAllCertificates() {
        return new ArrayList<>(mockCertificates);
    }

    public static List<Branch> getAllBranches() {
        return mockBranches;
    }
}
