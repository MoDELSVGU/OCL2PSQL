DROP DATABASE IF EXISTS stafftestdb;
CREATE DATABASE stafftestdb;
USE stafftestdb;
CREATE TABLE Staff (Staff_id INT (11) NOT NULL AUTO_INCREMENT PRIMARY KEY, name VARCHAR (100), age INT (11), salary INT (11));
CREATE TABLE Employment (employer INT (11), employees INT (11));
ALTER TABLE Employment ADD FOREIGN KEY (employer) REFERENCES Staff (Staff_id), ADD FOREIGN KEY (employees) REFERENCES Staff (Staff_id);
