import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

// BankAccount class represents a bank account
class BankAccount {
    private String accountNumber;
    private String password;
    private double balance;
    private ArrayList<String> transactionHistory;

    // Constructor for creating a bank account with account number, password, and last name
    public BankAccount(String accountNumber, String password, String lastName) {
        this.accountNumber = accountNumber;
        this.password = password;
        this.balance = 0.0;
        this.transactionHistory = new ArrayList<>();
    }

    // Constructor for creating a bank account with account number and password as integers
    public BankAccount(String accountNumber, String password) {
        this.accountNumber = accountNumber;
        this.password = String.valueOf(password);
        this.balance = 0.0;
        this.transactionHistory = new ArrayList<>();
    }

    // Getters and setters
    public String getAccountNumber() {
        return accountNumber;
    }

    public boolean validatePassword(String password) {
        return this.password.equals(password);
    }

    public double getBalance() {
        return balance;
    }

    // Deposit money into the account
    public void deposit(double amount) {
        balance += amount;
        transactionHistory.add("Deposit: +" + amount);
    }

    // Withdraw money from the account
    public boolean withdraw(double amount) {
        if (balance >= amount) {
            balance -= amount;
            transactionHistory.add("Withdrawal: -" + amount);
            return true;
        } else {
            System.out.println("Insufficient balance.");
            return false;
        }
    }

    // Display transaction history
    public void showTransactionHistory() {
        System.out.println("Transaction History:");
        for (String transaction : transactionHistory) {
            System.out.println(transaction);
        }
    }
}

// BankingManagementSystem class manages the banking operations
class BankingManagementSystem {
    private Connection connection;
    private Scanner scanner;
    private BankAccount currentUser;
    private BankAccount account;

    public BankingManagementSystem() {
        this.scanner = new Scanner(System.in);
        establishDatabaseConnection();
    }

    // Establish database connection
    private void establishDatabaseConnection() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank_information", "root", "creater123");
        } catch (SQLException e) {
            System.out.println("An error occurred while establishing the database connection: " + e.getMessage());
        }
    }

    // Close database connection
    private void closeDatabaseConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println("An error occurred while closing the database connection: " + e.getMessage());
            }
        }
    }

    // Perform a transaction (deposit or withdrawal)
    private void performTransaction(String accountNumber, String transactionType) throws SQLException {
        try {
            System.out.println("\n*** " + transactionType + " ***");
            System.out.print("Enter the amount: ");
            double amount = scanner.nextDouble();
            System.out.print("Enter a description: ");
            String description = scanner.nextLine();

            // Update account balance
            String updateBalanceQuery = "UPDATE accounts SET balance = balance + ? WHERE account_number = ?";
            PreparedStatement updateBalanceStatement = connection.prepareStatement(updateBalanceQuery);
            updateBalanceStatement.setDouble(1, amount);
            updateBalanceStatement.setString(2, accountNumber);
            updateBalanceStatement.executeUpdate();

            // Insert transaction record
            String insertTransactionQuery = "INSERT INTO transactions (account_number, transaction_type, amount, description) VALUES (?, ?, ?, ?)";
            PreparedStatement insertTransactionStatement = connection.prepareStatement(insertTransactionQuery);
            insertTransactionStatement.setString(1, accountNumber);
            insertTransactionStatement.setString(2, transactionType);
            insertTransactionStatement.setDouble(3, amount);
            insertTransactionStatement.setString(4, description);
            insertTransactionStatement.executeUpdate();

            System.out.println("Transaction completed successfully.");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Show the main menu
    void showMainMenu() throws SQLException {
        while (true) {
            System.out.println("\n*** Berhan Bank - Main Menu ***");
            System.out.println("1. Create Account");
            System.out.println("2. Login");
            System.out.println("3. Perform Transaction");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    createAccount();
                    break;
                case 2:
                    login();
                    performTransactions(currentUser);
                    break;
                case 3:
                    if (isLoggedIn()) {
                        initiateTransaction(account.getAccountNumber(), currentUser.getAccountNumber());
                    } else {
                        System.out.println("Please login first.");
                    }
                    break;
                case 4:
                    System.out.println("Thank you for using Berhan Bank. Goodbye!");
                    closeDatabaseConnection();
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }

    // Initiate a transaction (deposit or withdrawal)
    private void initiateTransaction(String number, String accountNumber) throws SQLException {
        System.out.println("\n====== Transaction Menu ======");
        System.out.println("1. Deposit");
        System.out.println("2. Withdraw");
        System.out.println("3. Return to main menu");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                performTransaction(accountNumber, "Deposit");
                break;
            case 2:
                performTransaction(accountNumber, "Withdraw");
                break;
            case 3:
                return;
            default:
                System.out.println("Invalid choice. Please try again.");
                break;
        }
    }

    // Check if a user is logged in
    private boolean isLoggedIn() {
        return currentUser != null;
    }

    // Create a new bank account
    private void createAccount() throws SQLException {
        System.out.println("\n============= Welcome to Berhan Bank ===========");
        System.out.println("\n====== Create a New Account ========");
        String accountNumber;

        // Prompt for account number and validate
        while (true) 
        {
            System.out.print("Enter account number: ");
            accountNumber = scanner.next();

            // Check if the account number already exists
            String checkAccountQuery = "SELECT * FROM accounts WHERE account_number = ?";
            PreparedStatement checkAccountStatement = connection.prepareStatement(checkAccountQuery);
            checkAccountStatement.setString(1, accountNumber);
            ResultSet accountResult = checkAccountStatement.executeQuery();

            if (accountResult.next()) {
                System.out.println("Account number already exists. Please enter a different account number.");
            } else {
                break;
            }
        }

        // Prompt for password
        System.out.print("Enter password: ");
        String password = scanner.next();

        // Prompt for last name
        System.out.print("Enter last name: ");
        String lastName = scanner.next();

        // Create a new BankAccount object
        BankAccount newAccount = new BankAccount(accountNumber, password, lastName);

        // Insert the new account into the database
        String insertAccountQuery = "INSERT INTO accounts (account_number, password, balance) VALUES (?, ?, ?)";
        PreparedStatement insertAccountStatement = connection.prepareStatement(insertAccountQuery);
        insertAccountStatement.setString(1, accountNumber);
        insertAccountStatement.setString(2, password);
        insertAccountStatement.setDouble(3, newAccount.getBalance());
        insertAccountStatement.executeUpdate();

        System.out.println("Account created successfully. Welcome to Berhan Bank!");

        // Set the current user to the newly created account
        currentUser = newAccount;
    }

    // Login to an existing account
    private void login() throws SQLException {
        System.out.println("\n====== Login ========");
        System.out.print("Enter account number: ");
        String accountNumber = scanner.next();
        System.out.print("Enter password: ");
        String password = scanner.next();

        // Retrieve the account details from the database
        String retrieveAccountQuery = "SELECT * FROM accounts WHERE account_number = ? AND password = ?";
        PreparedStatement retrieveAccountStatement = connection.prepareStatement(retrieveAccountQuery);
        retrieveAccountStatement.setString(1, accountNumber);
        retrieveAccountStatement.setString(2, password);
        ResultSet accountResult = retrieveAccountStatement.executeQuery();

        if (accountResult.next()) {
            // Create a BankAccount object for the logged-in user
            BankAccount account = new BankAccount(accountNumber, password);
            currentUser = account;
            System.out.println("Login successful. Welcome to Berhan Bank!");
        } else {
            System.out.println("Invalid account number or password. Please try again.");
        }
    }

    // Perform transactions (deposit or withdrawal) for the logged-in user
    private void performTransactions(BankAccount account) throws SQLException {
        if (account != null) {
            while (true) {
                System.out.println("\n*** Berhan Bank - Transaction Menu ***");
                System.out.println("1. Deposit");
                System.out.println("2. Withdraw");
                System.out.println("3. Show Transaction History");
                System.out.println("4. Return to main menu");
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        initiateTransaction(account.getAccountNumber(), "Deposit");
                        break;
                    case 2:
                        initiateTransaction(account.getAccountNumber(), "Withdraw");
                        break;
                    case 3:
                        account.showTransactionHistory();
                        break;
                    case 4:
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                        break;
                }
            }
        }
    }
}

public class banking{
    public static void main(String[] args) throws SQLException {
        BankingManagementSystem bankingSystem = new BankingManagementSystem();
        bankingSystem.showMainMenu();
    }
}
