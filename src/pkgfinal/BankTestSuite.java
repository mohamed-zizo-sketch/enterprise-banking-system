package pkgfinal;

import java.util.List;

/**
 * Automated Verification Test Suite for the Enterprise Banking System.
 * Tests core banking operations:
 * - Deposit & Withdrawal limits
 * - Overdraft protection enforcement in CurrentAccount
 * - Monthly interest accrual in SavingsAccount
 * - Inter-account fund transfers
 * - Certificate of deposit yield calculation
 */
public class BankTestSuite {

    public static void runAllTests() {
        int passed = 0;
        int failed = 0;

        System.out.println("==================================================================");
        System.out.println("       RUNNING ENTERPRISE BANKING AUTOMATED TEST SUITE            ");
        System.out.println("==================================================================");

        // Test 1: Savings Account Deposit and Interest
        try {
            SavingsAccount sa = new SavingsAccount(901, 10000.0, 0.12); // 12% annual
            sa.deposit(2000.0);
            if (sa.getBalance() != 12000.0) throw new AssertionError("Deposit failed");

            double monthlyInterest = sa.calculateMonthlyInterest(); // 12000 * (0.12 / 12) = 120
            if (Math.abs(monthlyInterest - 120.0) > 0.01) throw new AssertionError("Interest calculation failed");

            sa.applyMonthlyInterest();
            if (Math.abs(sa.getBalance() - 12120.0) > 0.01) throw new AssertionError("Interest application failed");

            System.out.println("[PASS] Test 1: Savings Account Deposit & Monthly Interest Accrual ✅");
            passed++;
        } catch (Exception e) {
            System.err.println("[FAIL] Test 1: " + e.getMessage());
            failed++;
        }

        // Test 2: Current Account Overdraft Protection
        try {
            CurrentAccount ca = new CurrentAccount(902, 500.0, 1000.0); // $500 bal, $1000 overdraft
            ca.withdraw(1200.0); // Should succeed, balance becomes -$700
            if (ca.getBalance() != -700.0) throw new AssertionError("Overdraft balance incorrect: " + ca.getBalance());

            boolean rejected = false;
            try {
                ca.withdraw(400.0); // Total withdrawal would be $1600 > $1500 max limit
            } catch (IllegalArgumentException ex) {
                rejected = true;
            }
            if (!rejected) throw new AssertionError("Overdraft exceeded limit without exception!");

            System.out.println("[PASS] Test 2: Current Account Overdraft Facility & Limit Enforcement ✅");
            passed++;
        } catch (Exception e) {
            System.err.println("[FAIL] Test 2: " + e.getMessage());
            failed++;
        }

        // Test 3: Inter-Account Fund Transfer
        try {
            DBManager.saveAccount(801, 5000.0, "Savings", 1);
            DBManager.saveAccount(802, 1000.0, "Current", 2);

            DBManager.transferFunds(801, 802, 1500.0);

            double bal801 = DBManager.getBalance(801);
            double bal802 = DBManager.getBalance(802);

            if (bal801 != 3500.0 || bal802 != 2500.0) {
                throw new AssertionError("Transfer balances incorrect. 801: " + bal801 + ", 802: " + bal802);
            }

            System.out.println("[PASS] Test 3: Inter-Account Atomic Fund Transfer ($1,500.00) ✅");
            passed++;
        } catch (Exception e) {
            System.err.println("[FAIL] Test 3: " + e.getMessage());
            failed++;
        }

        // Test 4: Certificate of Deposit Yield Calculation
        try {
            Certificate cert = new Certificate(701, 1, 100000.0, 0.15, 24); // 15% annual for 2 years
            double expectedMonthly = (100000.0 * 0.15) / 12.0; // 1250.0
            double expectedMaturity = 100000.0 + (100000.0 * 0.15 * 2.0); // 130,000.0

            if (Math.abs(cert.getMonthlyReturn() - expectedMonthly) > 0.01) {
                throw new AssertionError("Certificate monthly return calculation failed");
            }
            if (Math.abs(cert.getMaturityValue() - expectedMaturity) > 0.01) {
                throw new AssertionError("Certificate maturity value calculation failed");
            }

            System.out.println("[PASS] Test 4: Investment Certificate 15% APY Yield & Maturity Valuation ✅");
            passed++;
        } catch (Exception e) {
            System.err.println("[FAIL] Test 4: " + e.getMessage());
            failed++;
        }

        // Test 5: Negative Amount Validation
        try {
            SavingsAccount sa = new SavingsAccount(905, 5000.0, 0.05);
            boolean negDepositCaught = false;
            try {
                sa.deposit(-500.0);
            } catch (IllegalArgumentException e) {
                negDepositCaught = true;
            }
            if (!negDepositCaught) throw new AssertionError("Failed to catch negative deposit!");

            System.out.println("[PASS] Test 5: Strict Negative Amount Input Validation ✅");
            passed++;
        } catch (Exception e) {
            System.err.println("[FAIL] Test 5: " + e.getMessage());
            failed++;
        }

        System.out.println("\n--------------------------------------------------");
        System.out.printf("Test Results: %d Passed, %d Failed (Total %d)\n", passed, failed, (passed + failed));
        if (failed == 0) {
            System.out.println("STATUS: ALL BANKING SUITES PASSED! 🎉");
        } else {
            System.out.println("STATUS: SOME TESTS FAILED! ⚠️");
        }
        System.out.println("--------------------------------------------------");
    }
}
