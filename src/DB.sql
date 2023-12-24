use bank_information;
CREATE TABLE accounts (
  id INT AUTO_INCREMENT PRIMARY KEY,
  account_number VARCHAR(10) NOT NULL,
  password VARCHAR(255) NOT NULL,
  balance DECIMAL(10, 2) NOT NULL,
  first_name VARCHAR(255) NOT NULL,
  last_name VARCHAR(255) NOT NULL,
  job VARCHAR(255) NOT NULL,
  nationality VARCHAR(255) NOT NULL,
  sex VARCHAR(10) NOT NULL,
  mother_name VARCHAR(255) NOT NULL,
  UNIQUE (account_number)
);
select * from accounts;

CREATE TABLE transactions (
  id INT AUTO_INCREMENT PRIMARY KEY,
  account_number VARCHAR(10) NOT NULL,
  transaction_type ENUM('DEPOSIT') NOT NULL,
  amount DECIMAL(10, 2) NOT NULL,
  description VARCHAR(255),
  transaction_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (account_number) REFERENCES accounts(account_number)
);
select * from transactions;
