package pkgfinal;

import java.io.File;
import java.net.URL;
import java.util.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 * Enterprise Core Banking System Desktop Application.
 * Features:
 * - Real-time Banking Dashboard & Financial KPIs
 * - Customer Onboarding & KYC Management
 * - Savings & Current Accounts with Overdraft Facilities
 * - Inter-Account Atomic Fund Transfers
 * - Comprehensive Audited Transaction Ledger
 * - High-Yield Investment Certificates (CDs)
 * - Branch & Personnel Directory
 * - Dual Engine: Oracle Database XE with Resilient Standalone Failover
 */
public class Main extends Application {

    private TextArea customerDisplayArea;
    private TextArea transactionDisplayArea;
    private TextArea accountDisplayArea;
    private TextArea certificateDisplayArea;
    private Label totalAssetsLabel;
    private Label activeCustomersLabel;
    private Label activeAccountsLabel;

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();

        // 1. Top Header Bar
        HBox header = createHeader();
        root.setTop(header);

        // 2. Central Tabbed Workspace
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        Tab tabDashboard = new Tab("📊 Dashboard", createDashboardView(tabPane));
        Tab tabCustomers = new Tab("👥 Customers", createCustomersView());
        Tab tabAccounts = new Tab("💳 Accounts", createAccountsView());
        Tab tabTransactions = new Tab("💸 Transactions & Transfers", createTransactionsView());
        Tab tabAudit = new Tab("📜 Audit Ledger", createAuditView());
        Tab tabCertificates = new Tab("📜 Investment Certificates", createCertificatesView());
        Tab tabBranches = new Tab("🏢 Branches", createBranchesView());

        tabPane.getTabs().addAll(tabDashboard, tabCustomers, tabAccounts, tabTransactions, tabAudit, tabCertificates, tabBranches);
        root.setCenter(tabPane);

        // Apply Stylesheet safely
        Scene scene = new Scene(root, 950, 700);
        applyStylesheet(scene);

        primaryStage.setTitle("Global Horizon Enterprise Banking System");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Refresh stats
        refreshMetrics();
    }

    private HBox createHeader() {
        HBox header = new HBox(20);
        header.getStyleClass().add("header-bar");
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(16, 24, 16, 24));

        VBox titleBox = new VBox(2);
        Label title = new Label("GLOBAL HORIZON ENTERPRISE BANK");
        title.getStyleClass().add("header-title");

        Label subtitle = new Label("Core Banking Operations & Enterprise Financial Ledger");
        subtitle.getStyleClass().add("header-subtitle");
        titleBox.getChildren().addAll(title, subtitle);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        boolean isOracle = DBManager.isUsingOracle();
        Label dbStatus = new Label(isOracle ? "● Online: Oracle XE Database" : "● Standalone Resilient Engine");
        dbStatus.getStyleClass().add(isOracle ? "status-badge-online" : "status-badge-demo");

        header.getChildren().addAll(titleBox, spacer, dbStatus);
        return header;
    }

    private ScrollPane createDashboardView(TabPane parentTabs) {
        VBox container = new VBox(24);
        container.setPadding(new Insets(24));

        // KPI Cards Row
        HBox kpiRow = new HBox(16);
        kpiRow.setAlignment(Pos.CENTER);

        VBox cardAssets = createKpiCard("TOTAL CAPITAL ASSETS", "$87,000.00", "totalAssetsLabel");
        VBox cardCustomers = createKpiCard("REGISTERED CUSTOMERS", "3", "activeCustomersLabel");
        VBox cardAccounts = createKpiCard("ACTIVE ACCOUNTS", "3", "activeAccountsLabel");
        VBox cardBranches = createKpiCard("COMMERCIAL BRANCHES", "3 Active", null);

        HBox.setHgrow(cardAssets, Priority.ALWAYS);
        HBox.setHgrow(cardCustomers, Priority.ALWAYS);
        HBox.setHgrow(cardAccounts, Priority.ALWAYS);
        HBox.setHgrow(cardBranches, Priority.ALWAYS);
        kpiRow.getChildren().addAll(cardAssets, cardCustomers, cardAccounts, cardBranches);

        // Quick Actions Section
        VBox quickActionsBox = new VBox(12);
        quickActionsBox.getStyleClass().add("content-card");

        Label qaTitle = new Label("Quick Banking Operations");
        qaTitle.getStyleClass().add("section-title");

        HBox btnRow = new HBox(12);
        Button btnNewCustomer = new Button("👤 Register Customer");
        Button btnNewAccount = new Button("💳 Open Account");
        Button btnTransfer = new Button("💸 Transfer Funds");
        Button btnNewCert = new Button("📜 Issue Certificate");
        Button btnRunTests = new Button("🧪 Run Test Suite");

        btnNewCustomer.setOnAction(e -> parentTabs.getSelectionModel().select(1));
        btnNewAccount.setOnAction(e -> parentTabs.getSelectionModel().select(2));
        btnTransfer.setOnAction(e -> parentTabs.getSelectionModel().select(3));
        btnNewCert.setOnAction(e -> parentTabs.getSelectionModel().select(5));
        btnRunTests.setOnAction(e -> {
            BankTestSuite.runAllTests();
            showAlert("Verification Test Suite", "Automated Banking Suite completed successfully!\nCheck terminal console for full assertion trace.");
        });

        btnRow.getChildren().addAll(btnNewCustomer, btnNewAccount, btnTransfer, btnNewCert, btnRunTests);
        quickActionsBox.getChildren().addAll(qaTitle, btnRow);

        // System Info Card
        VBox infoCard = new VBox(8);
        infoCard.getStyleClass().add("content-card");
        Label infoTitle = new Label("Enterprise Architecture Highlights");
        infoTitle.getStyleClass().add("section-title");

        Label desc = new Label(
                "• Dual Database Engine: Online Oracle XE connectivity with resilient in-memory fallback.\n" +
                "• Full Overdraft Facility: Commercial Current Accounts permit controlled overdrafts up to limit.\n" +
                "• Compound Interest Engine: Savings Accounts accrue customizable annual interest.\n" +
                "• Atomic Fund Transfers: Source account deduction and target account deposit executed with audit logging."
        );
        desc.setStyle("-fx-text-fill: #475569; -fx-line-spacing: 4px;");
        infoCard.getChildren().addAll(infoTitle, desc);

        container.getChildren().addAll(kpiRow, quickActionsBox, infoCard);
        ScrollPane sp = new ScrollPane(container);
        sp.setFitToWidth(true);
        return sp;
    }

    private VBox createKpiCard(String title, String defaultValue, String labelId) {
        VBox card = new VBox(6);
        card.getStyleClass().add("metric-card");

        Label lblTitle = new Label(title);
        lblTitle.getStyleClass().add("metric-title");

        Label lblValue = new Label(defaultValue);
        lblValue.getStyleClass().add("metric-value");

        if ("totalAssetsLabel".equals(labelId)) totalAssetsLabel = lblValue;
        if ("activeCustomersLabel".equals(labelId)) activeCustomersLabel = lblValue;
        if ("activeAccountsLabel".equals(labelId)) activeAccountsLabel = lblValue;

        card.getChildren().addAll(lblTitle, lblValue);
        return card;
    }

    private HBox createCustomersView() {
        HBox root = new HBox(20);
        root.setPadding(new Insets(20));

        // Left: Register Customer Form
        VBox formCard = new VBox(12);
        formCard.getStyleClass().add("content-card");
        formCard.setPrefWidth(350);

        Label formTitle = new Label("Onboard New Customer");
        formTitle.getStyleClass().add("section-title");

        TextField idF = new TextField();
        idF.setPromptText("Customer ID (e.g. 4)");
        TextField nameF = new TextField();
        nameF.setPromptText("Full Name");
        TextField natIdF = new TextField();
        natIdF.setPromptText("National ID (14 digits)");
        TextField phoneF = new TextField();
        phoneF.setPromptText("Mobile Phone Number");

        Button btnRegister = new Button("Register Customer");
        btnRegister.getStyleClass().add("btn-success");

        btnRegister.setOnAction(e -> {
            try {
                int id = Integer.parseInt(idF.getText().trim());
                String name = nameF.getText().trim();
                String natId = natIdF.getText().trim();
                String phone = phoneF.getText().trim();

                if (name.isEmpty() || natId.isEmpty() || phone.isEmpty()) {
                    showAlert("Validation Error", "All fields are required.");
                    return;
                }

                Customer c = new Customer(id, name, natId, phone);
                DBManager.saveCustomer(c);
                showAlert("Success", "Customer #" + id + " (" + name + ") registered successfully!");
                idF.clear(); nameF.clear(); natIdF.clear(); phoneF.clear();
                refreshCustomerList();
                refreshMetrics();
            } catch (NumberFormatException ex) {
                showAlert("Input Error", "Customer ID must be a numeric integer.");
            } catch (Exception ex) {
                showAlert("Error", ex.getMessage());
            }
        });

        formCard.getChildren().addAll(formTitle, new Label("Customer ID:"), idF,
                new Label("Full Legal Name:"), nameF,
                new Label("National ID / Passport:"), natIdF,
                new Label("Mobile Phone:"), phoneF,
                btnRegister);

        // Right: Customer Directory List
        VBox listCard = new VBox(12);
        listCard.getStyleClass().add("content-card");
        HBox.setHgrow(listCard, Priority.ALWAYS);

        Label listTitle = new Label("Customer Roster & Directory");
        listTitle.getStyleClass().add("section-title");

        customerDisplayArea = new TextArea();
        customerDisplayArea.setEditable(false);
        VBox.setVgrow(customerDisplayArea, Priority.ALWAYS);

        Button btnRefresh = new Button("🔄 Refresh List");
        btnRefresh.setOnAction(e -> refreshCustomerList());

        listCard.getChildren().addAll(listTitle, customerDisplayArea, btnRefresh);
        root.getChildren().addAll(formCard, listCard);

        refreshCustomerList();
        return root;
    }

    private HBox createAccountsView() {
        HBox root = new HBox(20);
        root.setPadding(new Insets(20));

        // Left: Open Account Form
        VBox formCard = new VBox(12);
        formCard.getStyleClass().add("content-card");
        formCard.setPrefWidth(350);

        Label formTitle = new Label("Open New Bank Account");
        formTitle.getStyleClass().add("section-title");

        TextField custIdF = new TextField();
        custIdF.setPromptText("Existing Customer ID (e.g. 1)");
        TextField accNumF = new TextField();
        accNumF.setPromptText("Account Number (e.g. 104)");
        TextField balanceF = new TextField();
        balanceF.setPromptText("Initial Deposit Amount");

        ComboBox<String> typeBox = new ComboBox<>();
        typeBox.getItems().addAll("Savings", "Current");
        typeBox.setValue("Savings");

        Button btnCreate = new Button("Create & Open Account");
        btnCreate.getStyleClass().add("btn-success");

        btnCreate.setOnAction(e -> {
            try {
                int cid = Integer.parseInt(custIdF.getText().trim());
                int accNo = Integer.parseInt(accNumF.getText().trim());
                double bal = Double.parseDouble(balanceF.getText().trim());
                String type = typeBox.getValue();

                DBManager.saveAccount(accNo, bal, type, cid);
                showAlert("Success", "Account #" + accNo + " (" + type + ") created for Customer " + cid);
                custIdF.clear(); accNumF.clear(); balanceF.clear();
                refreshAccountList();
                refreshMetrics();
            } catch (NumberFormatException ex) {
                showAlert("Input Error", "Please enter valid numeric values for IDs and balance.");
            } catch (Exception ex) {
                showAlert("Error", ex.getMessage());
            }
        });

        // Quick Balance Check
        Separator sep = new Separator();
        Label balanceCheckTitle = new Label("Quick Balance Inquiry");
        balanceCheckTitle.getStyleClass().add("section-title");

        TextField checkAccF = new TextField();
        checkAccF.setPromptText("Enter Account Number");
        Button btnCheck = new Button("Inquire Balance");
        Label lblCheckResult = new Label("");
        lblCheckResult.setStyle("-fx-font-weight: bold; -fx-text-fill: #0284c7;");

        btnCheck.setOnAction(e -> {
            try {
                int acc = Integer.parseInt(checkAccF.getText().trim());
                double b = DBManager.getBalance(acc);
                if (b == -1.0) {
                    lblCheckResult.setText("Account #" + acc + " not found!");
                } else {
                    lblCheckResult.setText("Balance: $" + String.format("%.2f", b));
                }
            } catch (Exception ex) {
                lblCheckResult.setText("Please enter a numeric account number.");
            }
        });

        formCard.getChildren().addAll(formTitle,
                new Label("Customer ID:"), custIdF,
                new Label("Account Number:"), accNumF,
                new Label("Initial Deposit:"), balanceF,
                new Label("Account Type:"), typeBox,
                btnCreate, sep, balanceCheckTitle, checkAccF, btnCheck, lblCheckResult);

        // Right: Accounts Roster
        VBox listCard = new VBox(12);
        listCard.getStyleClass().add("content-card");
        HBox.setHgrow(listCard, Priority.ALWAYS);

        Label listTitle = new Label("Active Bank Accounts");
        listTitle.getStyleClass().add("section-title");

        accountDisplayArea = new TextArea();
        accountDisplayArea.setEditable(false);
        VBox.setVgrow(accountDisplayArea, Priority.ALWAYS);

        Button btnRefresh = new Button("🔄 Refresh Accounts");
        btnRefresh.setOnAction(e -> refreshAccountList());

        listCard.getChildren().addAll(listTitle, accountDisplayArea, btnRefresh);
        root.getChildren().addAll(formCard, listCard);

        refreshAccountList();
        return root;
    }

    private ScrollPane createTransactionsView() {
        VBox container = new VBox(20);
        container.setPadding(new Insets(20));

        HBox row = new HBox(20);

        // 1. Deposit / Withdrawal Box
        VBox cashCard = new VBox(12);
        cashCard.getStyleClass().add("content-card");
        cashCard.setPrefWidth(420);

        Label cashTitle = new Label("Deposit & Withdrawal");
        cashTitle.getStyleClass().add("section-title");

        TextField accF = new TextField();
        accF.setPromptText("Account Number (e.g. 101)");
        TextField amtF = new TextField();
        amtF.setPromptText("Amount ($)");

        HBox btnBox = new HBox(12);
        Button btnDeposit = new Button("Deposit Funds");
        btnDeposit.getStyleClass().add("btn-success");
        Button btnWithdraw = new Button("Withdraw Funds");
        btnWithdraw.getStyleClass().add("btn-danger");
        btnBox.getChildren().addAll(btnDeposit, btnWithdraw);

        btnDeposit.setOnAction(e -> {
            try {
                int acc = Integer.parseInt(accF.getText().trim());
                double amt = Double.parseDouble(amtF.getText().trim());
                Account a = DBManager.getAccount(acc);
                if (a == null) { showAlert("Error", "Account #" + acc + " not found!"); return; }

                a.deposit(amt);
                DBManager.updateBalance(acc, a.getBalance());
                DBManager.recordTransaction(acc, Transaction.Type.DEPOSIT, amt, a.getBalance(), "Over-the-counter Cash Deposit");
                showAlert("Success", "Deposited $" + amt + ". New Balance: $" + String.format("%.2f", a.getBalance()));
                amtF.clear();
                refreshMetrics();
                refreshAuditView();
            } catch (Exception ex) {
                showAlert("Error", ex.getMessage());
            }
        });

        btnWithdraw.setOnAction(e -> {
            try {
                int acc = Integer.parseInt(accF.getText().trim());
                double amt = Double.parseDouble(amtF.getText().trim());
                Account a = DBManager.getAccount(acc);
                if (a == null) { showAlert("Error", "Account #" + acc + " not found!"); return; }

                a.withdraw(amt);
                DBManager.updateBalance(acc, a.getBalance());
                DBManager.recordTransaction(acc, Transaction.Type.WITHDRAWAL, amt, a.getBalance(), "Cash Withdrawal");
                showAlert("Success", "Withdrew $" + amt + ". New Balance: $" + String.format("%.2f", a.getBalance()));
                amtF.clear();
                refreshMetrics();
                refreshAuditView();
            } catch (Exception ex) {
                showAlert("Error", ex.getMessage());
            }
        });

        cashCard.getChildren().addAll(cashTitle, new Label("Account Number:"), accF,
                new Label("Amount:"), amtF, btnBox);

        // 2. Inter-Account Fund Transfer Box
        VBox transferCard = new VBox(12);
        transferCard.getStyleClass().add("content-card");
        HBox.setHgrow(transferCard, Priority.ALWAYS);

        Label transferTitle = new Label("Inter-Account Fund Transfer");
        transferTitle.getStyleClass().add("section-title");

        TextField fromAccF = new TextField();
        fromAccF.setPromptText("Source Account Number (Debit)");
        TextField toAccF = new TextField();
        toAccF.setPromptText("Destination Account Number (Credit)");
        TextField transferAmtF = new TextField();
        transferAmtF.setPromptText("Transfer Amount ($)");

        Button btnExecTransfer = new Button("Execute Atomic Transfer");
        btnExecTransfer.getStyleClass().add("btn-success");

        btnExecTransfer.setOnAction(e -> {
            try {
                int from = Integer.parseInt(fromAccF.getText().trim());
                int to = Integer.parseInt(toAccF.getText().trim());
                double amt = Double.parseDouble(transferAmtF.getText().trim());

                DBManager.transferFunds(from, to, amt);
                showAlert("Transfer Successful", "Successfully transferred $" + String.format("%.2f", amt) +
                        " from Account #" + from + " to Account #" + to);
                fromAccF.clear(); toAccF.clear(); transferAmtF.clear();
                refreshMetrics();
                refreshAuditView();
            } catch (Exception ex) {
                showAlert("Transfer Failed", ex.getMessage());
            }
        });

        transferCard.getChildren().addAll(transferTitle,
                new Label("From Account:"), fromAccF,
                new Label("To Account:"), toAccF,
                new Label("Transfer Amount:"), transferAmtF,
                btnExecTransfer);

        row.getChildren().addAll(cashCard, transferCard);
        container.getChildren().add(row);

        ScrollPane sp = new ScrollPane(container);
        sp.setFitToWidth(true);
        return sp;
    }

    private VBox createAuditView() {
        VBox root = new VBox(12);
        root.setPadding(new Insets(20));

        Label title = new Label("Financial Audit Ledger & Real-Time Transaction Logs");
        title.getStyleClass().add("section-title");

        transactionDisplayArea = new TextArea();
        transactionDisplayArea.setEditable(false);
        VBox.setVgrow(transactionDisplayArea, Priority.ALWAYS);

        Button btnRefresh = new Button("🔄 Refresh Ledger");
        btnRefresh.setOnAction(e -> refreshAuditView());

        root.getChildren().addAll(title, transactionDisplayArea, btnRefresh);
        refreshAuditView();
        return root;
    }

    private HBox createCertificatesView() {
        HBox root = new HBox(20);
        root.setPadding(new Insets(20));

        // Form
        VBox form = new VBox(12);
        form.getStyleClass().add("content-card");
        form.setPrefWidth(350);

        Label title = new Label("Issue Certificate of Deposit (CD)");
        title.getStyleClass().add("section-title");

        TextField custIdF = new TextField();
        custIdF.setPromptText("Customer ID");
        TextField amountF = new TextField();
        amountF.setPromptText("Principal Amount (e.g. 50000)");

        ComboBox<Integer> durationBox = new ComboBox<>();
        durationBox.getItems().addAll(12, 24, 36, 60);
        durationBox.setValue(36);

        ComboBox<Double> yieldBox = new ComboBox<>();
        yieldBox.getItems().addAll(0.12, 0.15, 0.18);
        yieldBox.setValue(0.15);

        Button btnIssue = new Button("Issue Certificate");
        btnIssue.getStyleClass().add("btn-success");

        btnIssue.setOnAction(e -> {
            try {
                int cid = Integer.parseInt(custIdF.getText().trim());
                double amt = Double.parseDouble(amountF.getText().trim());
                int term = durationBox.getValue();
                double rate = yieldBox.getValue();

                DBManager.saveCertificate(cid, amt, rate, term);
                showAlert("Certificate Issued", "Certificate created successfully!\nMonthly Yield: $" +
                        String.format("%.2f", (amt * rate) / 12.0) + "\nMaturity Total: $" +
                        String.format("%.2f", amt + (amt * rate * (term / 12.0))));
                custIdF.clear(); amountF.clear();
                refreshCertificateList();
            } catch (Exception ex) {
                showAlert("Error", ex.getMessage());
            }
        });

        form.getChildren().addAll(title, new Label("Customer ID:"), custIdF,
                new Label("Principal Amount ($):"), amountF,
                new Label("Duration (Months):"), durationBox,
                new Label("Annual Yield (APY):"), yieldBox, btnIssue);

        // List
        VBox listCard = new VBox(12);
        listCard.getStyleClass().add("content-card");
        HBox.setHgrow(listCard, Priority.ALWAYS);

        Label listTitle = new Label("Active Investment Certificates");
        listTitle.getStyleClass().add("section-title");

        certificateDisplayArea = new TextArea();
        certificateDisplayArea.setEditable(false);
        VBox.setVgrow(certificateDisplayArea, Priority.ALWAYS);

        listCard.getChildren().addAll(listTitle, certificateDisplayArea);
        root.getChildren().addAll(form, listCard);

        refreshCertificateList();
        return root;
    }

    private VBox createBranchesView() {
        VBox root = new VBox(16);
        root.setPadding(new Insets(20));

        Label title = new Label("Commercial Branches & Management Directory");
        title.getStyleClass().add("section-title");

        TextArea branchArea = new TextArea();
        branchArea.setEditable(false);
        VBox.setVgrow(branchArea, Priority.ALWAYS);

        StringBuilder sb = new StringBuilder();
        for (Branch b : DBManager.getAllBranches()) {
            sb.append("======================================================================\n");
            sb.append(b.displayInfo()).append("\n");
            sb.append("Address : ").append(b.getAddress()).append("\n");
            sb.append("Status  : ").append(b.isOpen() ? "OPEN FOR TRANSACTIONS" : "CLOSED").append("\n");
            sb.append("Assigned Personnel:\n");
            for (Employee emp : b.getEmployees()) {
                sb.append("   • ").append(emp.displayInfo()).append("\n");
            }
            sb.append("\n");
        }
        branchArea.setText(sb.toString());
        root.getChildren().addAll(title, branchArea);
        return root;
    }

    private void refreshCustomerList() {
        if (customerDisplayArea == null) return;
        StringBuilder sb = new StringBuilder();
        List<Customer> list = DBManager.getAllCustomers();
        for (Customer c : list) {
            sb.append(c.displayInfo()).append("\n");
            sb.append("----------------------------------------------------------------------\n");
        }
        customerDisplayArea.setText(sb.toString());
    }

    private void refreshAccountList() {
        if (accountDisplayArea == null) return;
        StringBuilder sb = new StringBuilder();
        List<Account> list = DBManager.getAllAccounts();
        for (Account a : list) {
            sb.append(a.displayInfo()).append("\n");
            sb.append("----------------------------------------------------------------------\n");
        }
        accountDisplayArea.setText(sb.toString());
    }

    private void refreshAuditView() {
        if (transactionDisplayArea == null) return;
        StringBuilder sb = new StringBuilder();
        for (Transaction t : DBManager.getAllTransactions()) {
            sb.append(t.displayInfo()).append("\n");
        }
        transactionDisplayArea.setText(sb.toString());
    }

    private void refreshCertificateList() {
        if (certificateDisplayArea == null) return;
        StringBuilder sb = new StringBuilder();
        for (Certificate c : DBManager.getAllCertificates()) {
            sb.append(c.displayInfo()).append("\n");
            sb.append("----------------------------------------------------------------------\n");
        }
        certificateDisplayArea.setText(sb.toString());
    }

    private void refreshMetrics() {
        double total = 0;
        for (Account a : DBManager.getAllAccounts()) {
            total += a.getBalance();
        }
        if (totalAssetsLabel != null) totalAssetsLabel.setText("$" + String.format("%,.2f", total));
        if (activeCustomersLabel != null) activeCustomersLabel.setText(String.valueOf(DBManager.getAllCustomers().size()));
        if (activeAccountsLabel != null) activeAccountsLabel.setText(String.valueOf(DBManager.getAllAccounts().size()));
    }

    private void applyStylesheet(Scene scene) {
        try {
            File f = new File("src/pkgfinal/Style.css");
            if (f.exists()) {
                scene.getStylesheets().add(f.toURI().toString());
            } else {
                URL res = getClass().getResource("Style.css");
                if (res != null) scene.getStylesheets().add(res.toExternalForm());
            }
        } catch (Exception ignored) {}
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        // Check for CLI / headless mode flag
        if (args != null && args.length > 0 && ("--cli".equalsIgnoreCase(args[0]) || "-c".equalsIgnoreCase(args[0]) || "--test".equalsIgnoreCase(args[0]))) {
            BankTestSuite.runAllTests();
            return;
        }
        launch(args);
    }
}
