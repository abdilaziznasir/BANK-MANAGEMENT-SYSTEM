BankManagement system
It is group project given us by our instructor

Berhan Bank - Banking Management System The Berhan Bank - Banking Management System is a Java-based application that provides a user-friendly interface for managing bank accounts. It allows users to create accounts, login, perform transactions, and view account information. This system is designed to be simple and easy to use, making it suitable for both customers and bank employees.

Features
Create a new bank account with a unique account number and password. Login to an existing account using the account number and password. Perform transactions such as depositing money into the account and withdrawing money (if sufficient balance is available). Check the current balance of the account. View the transaction history of the account. Secure password storage using hashing and salt techniques. Error handling for invalid inputs and database connection issues.

Technologies Used
Java MySQL JDBC (Java Database Connectivity)

Setup and Usage
Clone the repository to your local machine. Ensure you have Java and MySQL installed on your system. Create a MySQL database named bank_information. Update the database connection details in the code (establishDatabaseConnection method) to match your MySQL server configuration. Compile and run the Main class to start the Banking Management System. Follow the on-screen prompts to create accounts, login, and perform transactions. Database Structure The application uses a MySQL database to store account information and transaction history. The database has the following structure:

accounts table:
account_number (Primary Key, VARCHAR(10)): The unique account number of the bank account. password_hash (VARCHAR(255)): The hashed password for the account. password_salt (VARCHAR(255)): The salt used for password hashing. balance (DOUBLE): The current balance of the account. transactions table:

id (Primary Key, INT): The unique identifier of the transaction. account_number (Foreign Key, VARCHAR(10)): The account number of the bank account associated with the transaction. transaction_type (VARCHAR(255)): The type of transaction (e.g., "Deposit", "Withdraw"). amount (DOUBLE): The amount of the transaction. description (VARCHAR(255)): A description of the transaction. timestamp (TIMESTAMP): The timestamp of the transaction.

Sample Usage
Here's an example of how to use the Berhan Bank - Banking Management System:

Run the application and select "Create Account" from the main menu. Enter a 10-digit account number and a password for the new account. Return to the main menu and select "Login". Enter the account number and password for the account created in the previous step. Once logged in, select "Deposit" or "Withdraw" to perform a transaction. Select "Check Balance" to view the current balance of the account. Select "Transaction History" to view the transaction history of the account. Logout when finished. Contribution Contributions to this project are welcome! If you find any issues or have suggestions for improvements, please feel free to create a pull request or submit an issue.

License
This project is licensed under the MIT License. You are free to use, modify, and distribute this code as per the terms of the license.

Acknowledgements
This project was developed as a learning exercise and does not represent a real banking system. Special thanks to Berhan Bank for providing the inspiration for this project.
