import java.sql.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

class BankAccount {
    private String accountNumber;
    private String password;
    private double balance;
    private ArrayList<String> transactionHistory;

    public BankAccount(String accountNumber, String password, String lastName) {
        this.accountNumber = accountNumber;
        this.password = password;
        this.balance = 0.0;
        this.transactionHistory = new ArrayList<>();
    }


public BankAccount(String accountNumber, int password) {
            this.accountNumber = accountNumber;
            this.password = String.valueOf(password);

            this.transactionHistory = new ArrayList<>(); // Initialize transactionHistory here
        }



    public String getAccountNumber() {
        return accountNumber;
    }

    public boolean validatePassword(String password) {
        return this.password.equals(password);
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        balance += amount;
        transactionHistory.add("Deposit: +" + amount);
    }

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

    public void showTransactionHistory() {
        System.out.println("Transaction History:");
        for (String transaction : transactionHistory) {
            System.out.println(transaction);
        }
    }
}

class BankingManagementSystem {
    private Connection connection;
    private Scanner scanner;
    private BankAccount currentUser;

    public BankingManagementSystem() {
        this.scanner = new Scanner(System.in);
        establishDatabaseConnection();
    }
    private void establishDatabaseConnection() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank_information", "root", "amazing2123&");
        } catch (SQLException e) {
            System.out.println("An error occurred while establishing the database connection: " + e.getMessage());
        }
    }
    private void closeDatabaseConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.out.println("An error occurred while closing the database connection: " + e.getMessage());
            }
        }
    }
    private void performTransaction(String accountNumber, String transactionType) throws SQLException {

        String transactionType1 = transactionType;
        {
            try {
                System.out.println("\n*** " + transactionType1 + " ***");
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
                insertTransactionStatement.setString(2, transactionType1);
                insertTransactionStatement.setDouble(3, amount);
                insertTransactionStatement.setString(4, description);
                insertTransactionStatement.executeUpdate();

                System.out.println("Transaction completed successfully.");

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }
    }


    void showMainMenu() throws SQLException {
        while (true) {
            System.out.println("\n---------------------select the choice -------------------");
            System.out.println("1. Create Account");
            System.out.println("2. Login");
            System.out.println("3. Perform Transaction");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");

            int choice;
            try {
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character after reading the integer

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
                            initiateTransaction(currentUser.getAccountNumber());
                        } else {
                            System.out.println("Please login first.");
                        }
                        break;
                    case 4:
                        System.out.println("Thank you for using the banking management system. Goodbye!");
                        closeDatabaseConnection();
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a valid integer choice.");
                scanner.nextLine(); // Consume the invalid input
            }
        }
    }

    private void initiateTransaction(String accountNumber) throws SQLException {

        System.out.println("\n------------------ Transaction Menu ----------------");
        System.out.println("1. Deposit");

        System.out.println("3. Return to main menu");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                performTransaction(accountNumber, "Deposit");
                break;

            case 2:
                return;
            default:
                System.out.println("Invalid choice. Please try again.");
                break;
        }
    }



    private boolean isLoggedIn() {
        return currentUser != null;
    }
    private void createAccount() throws SQLException {
        System.out.println("\n------------------- Welcome to Programmers Bank -------------------");
        System.out.println("\n====== Create a New Account ========");
        String accountNumber = null;

        // Prompt for account number and validate
        while (true) {
            System.out.print("Enter account number (10 digits): ");
            accountNumber = scanner.next();
            if (accountNumber.matches("\\d{10}")) {
                break;  // Break the loop if the account number is 10 digits
            } else {
                System.out.println("Invalid account number. Please enter a 10-digit account number.");
            }
        }

        int passwordAttempts = 0;
        String password = null;

        // Loop for password input with a minimum length of 5 characters
        while (passwordAttempts < 3) {
            System.out.print("Set password (at least 5 characters): ");
            password = scanner.next(); // Using nextLine() to accept spaces and multiple characters

            if (password.length() < 5) {
                System.out.println("Password should be at least 5 characters long.");
                passwordAttempts++;

                if (passwordAttempts == 3) {
                    System.out.println("Maximum attempts reached. Returning to the main menu.");
                    return; // Return to the main menu
                }
            } else {
                break; // Exit the loop if the password meets the criteria
            }
        }

        scanner.nextLine(); // Consume the newline character after reading password

        String firstName = getStringInput("Enter first name: ");
        String lastName = getStringInput("Enter last name: ");
        String job = getStringInput("Enter job: ");
        String nationality = getStringInput("Enter nationality: ");
        String motherName = getStringInput("Enter mother's name: ");

        String insertQuery = "INSERT INTO accounts (account_number, password, balance, first_name, last_name, job, nationality, sex, mother_name) VALUES (?, ?, 0, ?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
        preparedStatement.setString(1, accountNumber);
        preparedStatement.setString(2, password);
        preparedStatement.setString(3, firstName);
        preparedStatement.setString(4, lastName);
        preparedStatement.setString(5, job);
        preparedStatement.setString(6, nationality);
        preparedStatement.setString(7, ""); // Assuming the 'sex' column is not used in this code
        preparedStatement.setString(8, motherName);
        preparedStatement.executeUpdate();

        System.out.println("Account created successfully.");
    }

    private String getStringInput(String prompt) {
        String input;

        while (true) {
            System.out.print(prompt);
            input = scanner.nextLine();

            if (!input.isEmpty() && input.matches("[a-zA-Z]+")) {
                break; // Break the loop if the input is not empty and contains only alphabetic characters
            } else {
                System.out.println("Invalid input. Please enter a valid string.");
            }
        }

        return input;
    }

    private void login() throws SQLException {
        int loginAttempts = 0;
        boolean loggedIn = false;

        while (loginAttempts < 3 && !loggedIn) {
            System.out.print("Enter your account number: ");
            String accountNumber = scanner.next();

            // Check if the account number exists in the database
            String checkAccountQuery = "SELECT * FROM accounts WHERE account_number = ?";
            PreparedStatement checkAccountStatement = connection.prepareStatement(checkAccountQuery);
            checkAccountStatement.setString(1, accountNumber);
            ResultSet accountResult = checkAccountStatement.executeQuery();

            if (accountResult.next()) {
                System.out.print("Enter your password: ");
                int password = scanner.nextInt();

                // Verify the password
                int storedPassword = accountResult.getInt("password"); // Ensure column name matches your database
                if (password == storedPassword) {
                    // Login successful
                    String firstName = accountResult.getString("first_name");
                    String lastName = accountResult.getString("last_name");
                    currentUser = new BankAccount(accountNumber, password);
                    // Create a BankAccount object for the logged-in user
                    System.out.println("Login successful. Welcome, " + firstName + " " + lastName + "!");
                    loggedIn = true;
                } else {
                    System.out.println("Incorrect password. Please try again.");
                }
            } else {
                System.out.println("Account not found. Please try again.");
            }

            loginAttempts++;
        }

        if (!loggedIn) {
            System.out.println("Maximum login attempts reached. Please try again later.");
        }
//        performTransactions();
    }
    private void performTransactions(BankAccount account) throws SQLException {
        int choice;
        do {
            System.out.println("\n----------------- Welcome to your Account ------------------");
            System.out.println("1. View Balance");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. View Transaction History");
            System.out.println("5. Logout");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();


            switch (choice) {
                case 1:
                    viewBalance(account);
                    break;
                case 2:
                    deposit(account);
                    break;
                case 3:
                    try {
                        withdraw(account);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case 4:
                    viewTransactionHistory(account);
                    break;
                case 5:
                    System.out.println("Logged out successfully.");
                    break;
                default:
                    System.out.println("Invalid choice! Please enter a valid choice.");
                    break;
            }
        } while (choice != 5);

    }

    private void viewBalance(BankAccount account) {
        System.out.println("\nAccount Balance: " + account.getBalance());
    }

    private void deposit(BankAccount account) throws SQLException {
        System.out.print("Enter amount to deposit: ");
        double amount = scanner.nextDouble();

        String updateQuery = "UPDATE accounts SET balance = balance + ? WHERE account_number = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
        preparedStatement.setDouble(1, amount);
        preparedStatement.setString(2, account.getAccountNumber());
        preparedStatement.executeUpdate();

        account.deposit(amount);

        System.out.println("Amount deposited successfully.");
    }


    private void withdraw(BankAccount account) throws SQLException {

        System.out.print("Enter amount to withdraw: ");
        double amount = scanner.nextDouble();

        String updateQuery = "UPDATE accounts SET balance = balance - ? WHERE account_number = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
        preparedStatement.setDouble(1, amount);
        preparedStatement.setString(2, account.getAccountNumber());
        int rowsAffected = preparedStatement.executeUpdate();

        if (rowsAffected > 0) {
            account.withdraw(amount);
            System.out.println("Amount withdrawn successfully.");
        } else {
            System.out.println("Insufficient balance.");
        }
    }

    private void viewTransactionHistory(BankAccount account) {

        account.showTransactionHistory();
    }

    private record User(String accountNumber, String firstName, String lastName) {
        // account.showTransactionHistory();
        public String getAccountNumber() {

            return null;
        }

    }
}

public class Main {
    public static void main(String[] args) throws SQLException {
        BankingManagementSystem bankingManagementSystem = new BankingManagementSystem();
        bankingManagementSystem.showMainMenu();
    }
}
