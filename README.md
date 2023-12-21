Banking Management System

This is a simple banking management system implemented in Java. It provides basic functionalities such as creating accounts, logging in, performing transactions, viewing account balance, and viewing transaction history.

Features


  User can create a new account by providing account details such as account number, 
password, first name, last name, job, nationality, sex, and mother's name.
  User can log in to their account using the account number and password.
  Once logged in, users can perform transactions such as deposit and withdrawal.
  Users can view their account balance.
  Users can view their transaction history.
  Users can log out of their account.

Prerequisites

Java language
MySQL database


contributors

1.Amanuel Alemayehu
2.Abdilaziz nasir
3.Barekegn Asefa
4.Biniam Birhanu
5.Bacha Solomon

FEATURE
Account Management: Create new accounts with 
unique account numbers and passwords.
Login System: Log in using account numbers and passwords.
Transaction Handling: Deposit and withdraw funds, 
maintaining transaction histories.
Balance Inquiry: Check account balances at any time.

Database
The application uses a MySQL database to 
store account information and transaction records. 
Make sure to set up your database configuration in the code:

Database URL: jdbc:mysql://localhost:3306/bank_information
Username: root
Password: your_password
The SQL scripts to create the necessary tables 
(accounts and transactions) are provided in the 
database_scripts directory