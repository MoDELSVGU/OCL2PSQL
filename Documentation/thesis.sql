/*
 * Database
 */
CREATE DATABASE thesis_test;
use thesis_test;

DROP TABLE IF EXISTS company;
CREATE TABLE company(
  company_id mediumint NOT NULL AUTO_INCREMENT PRIMARY KEY,
  name varchar(100)
) ENGINE=InnoDB;

DROP TABLE IF EXISTS car;
CREATE TABLE car(
  car_id mediumint NOT NULL AUTO_INCREMENT PRIMARY KEY,
  model varchar(100),
  color varchar(100),
  price decimal
) ENGINE=InnoDB;

DROP TABLE IF EXISTS person;
CREATE TABLE person(
  person_id mediumint NOT NULL AUTO_INCREMENT PRIMARY KEY,
  name varchar(100),
  surname varchar(100),
  age tinyint
) ENGINE=InnoDB;

DROP TABLE IF EXISTS employee;
CREATE TABLE employee(
  employee_id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
  salary int,
  person mediumint,
  FOREIGN KEY (person) 
     REFERENCES person(person_id)
) ENGINE=InnoDB;

DROP TABLE IF EXISTS customer;
CREATE TABLE customer(
  customer_id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
  salary int,
  person mediumint,
  FOREIGN KEY (person) 
     REFERENCES person(person_id)
) ENGINE=InnoDB;


DROP TABLE IF EXISTS car_person;
CREATE TABLE car_person (
  car mediumint,
  person mediumint,
  FOREIGN KEY (car) 
     REFERENCES car(car_id),
  FOREIGN KEY (person) 
     REFERENCES person(person_id)
) ENGINE=InnoDB;

DROP TABLE IF EXISTS company_person;
CREATE TABLE company_person (
  company mediumint,
  person mediumint,
  FOREIGN KEY (company) 
     REFERENCES company(company_id),
  FOREIGN KEY (person) 
     REFERENCES person(person_id)
) ENGINE=InnoDB;


