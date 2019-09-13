/*
 * VGU
 */
CREATE DATABASE aliceDB;
use aliceDB;


DROP TABLE IF EXISTS role;
CREATE TABLE role(
  role_id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
  name varchar(100)
);

INSERT role(name)VALUES("Alice");
INSERT role(name)VALUES("Others");

DROP TABLE IF EXISTS employee;
CREATE TABLE employee(
  employee_id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
  name varchar(100),
  age int,
  salary int,
  role int NOT NULL
);


INSERT INTO employee(name, age, salary, role) 
   VALUES ("Alice", 22,  30000, 1);
INSERT INTO employee(name, age, salary, role) 
   VALUES ("Bob", 45,  65000, 2);
INSERT INTO employee(name, age, salary, role) 
   VALUES ("Carl", 34,  40000, 2);
INSERT INTO employee(name, age, salary, role) 
   VALUES ("Diane", 28,  55000, 2);
 


