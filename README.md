  # Banking Management System
This is a simple banking management system implemented in Java. It allows users to create bank accounts, login, perform transactions such as deposits and withdrawals, and view transaction history.

 # Features
Create a new account with a 10-digit account number and a password.
Login to an existing account using the account number and password.
Perform transactions such as deposit and withdrawal.
View transaction history for a specific account.
Technologies Used
Java
MySQL
JDBC
 # How to Use
Clone the repository to your local machine.
Open the project in your preferred Java IDE.
Make sure you have a MySQL database set up and running.
Update the database connection details in the establishDatabaseConnection() method in the BankingManagementSystem class.
Run the Main class to start the application.
Follow the prompts in the console to navigate through the banking management system.
Database Schema
The application uses a MySQL database with the following schema:

 # Table: accounts

Columns:
account_number (VARCHAR)
password (VARCHAR)
balance (DOUBLE)
first_name (VARCHAR)
last_name (VARCHAR)
job (VARCHAR)
nationality (VARCHAR)
sex (VARCHAR)
mother_name (VARCHAR)

 # Table: transactions

Columns:
account_number (VARCHAR)
transaction_type (VARCHAR)
amount (DOUBLE)
description (VARCHAR)
  # Contribution
Contributions to this project are welcome. You can fork the repository and make enhancements or bug fixes. Submit a pull request to merge your changes into the main branch.
