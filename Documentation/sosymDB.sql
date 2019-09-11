CREATE DATABASE sosymDB107;
use sosymDB107;

DROP TABLE IF EXISTS Company;
CREATE TABLE Company (
Company_id int NOT NULL AUTO_INCREMENT PRIMARY KEY, 
Company_ref varchar(50));

ALTER TABLE Company ADD UNIQUE (Company_ref);

ALTER TABLE Company ADD COLUMN name text;

DROP TABLE IF EXISTS Person;
CREATE TABLE Person (
Person_id int NOT NULL AUTO_INCREMENT PRIMARY KEY, 
Person_ref varchar(50));

ALTER TABLE Person ADD UNIQUE (Person_ref);

# CREATE INDEX index_name ON Person (age); 
ALTER TABLE Person ADD INDEX person_age (age)

ALTER TABLE Person ADD COLUMN name text;
ALTER TABLE Person ADD COLUMN surname text;
ALTER TABLE Person ADD COLUMN age int;

DROP TABLE IF EXISTS Car;
CREATE TABLE Car (
Car_id int NOT NULL AUTO_INCREMENT PRIMARY KEY, 
Car_ref varchar(50));

ALTER TABLE Car ADD UNIQUE (Car_ref);

ALTER TABLE Car ADD COLUMN model text;
ALTER TABLE Car ADD COLUMN color text;
ALTER TABLE Car ADD COLUMN age int;

DROP TABLE IF EXISTS Employee;
CREATE TABLE Employee (
Employee_id int NOT NULL AUTO_INCREMENT PRIMARY KEY, 
Employee_ref varchar(50));

ALTER TABLE Employee ADD UNIQUE (Employee_ref);

ALTER TABLE Employee ADD COLUMN Person int;
ALTER TABLE Employee ADD FOREIGN KEY fk_Person(Person) REFERENCES Person(Person_id);
ALTER TABLE Employee ADD COLUMN salary int;

DROP TABLE IF EXISTS Customer;
CREATE TABLE Customer (
Customer_id int NOT NULL AUTO_INCREMENT PRIMARY KEY, 
Customer_ref varchar(50));

ALTER TABLE Customer ADD UNIQUE (Customer_ref);

ALTER TABLE Customer ADD COLUMN Person int;
ALTER TABLE Customer ADD FOREIGN KEY fk_Person(Person) REFERENCES Person(Person_id);

DROP TABLE IF EXISTS people;
CREATE TABLE people ( 
employees int,
companies int,
FOREIGN KEY fk_people_employees(employees) REFERENCES Person(Person_id), 
FOREIGN KEY fk_people_companies(companies) REFERENCES Company(Company_id));

DROP TABLE IF EXISTS ownership;
CREATE TABLE ownership ( 
ownedCars int,
owners int,
FOREIGN KEY fk_ownership_ownedCars(ownedCars) REFERENCES Car(Car_id), 
FOREIGN KEY fk_ownership_owners(owners) REFERENCES  Person(Person_id));

/* scenario 
 - Person {(name = Hoang, surname = Nguyen, age= 25), (name = Nam, surname = Bui, age= 30)}
 - Employees { ([Hoang], salary = 15000000), ([Nam], salary=14000000)}
 - Cars {(model=city, color=black, age=5)}
 - ownership{("HoangNguyen", "CityBlack")}
 */
INSERT INTO Employee (Employee_ref) VALUES ("HoangNguyen");
INSERT INTO Person (Person_ref) VALUES ("HoangNguyen");
UPDATE Employee SET Person = (SELECT Person_id FROM Person WHERE Person_ref = "HoangNguyen") WHERE Employee_ref = "HoangNguyen";
UPDATE Person SET name = "Hoang" WHERE Person_ref = "HoangNguyen";
UPDATE Person SET surname = "Nguyen" WHERE Person_ref = "HoangNguyen";
UPDATE Person SET age = 25 WHERE Person_ref = "HoangNguyen";


INSERT INTO Employee (Employee_ref) VALUES ("NamBui");
INSERT INTO Person (Person_ref) VALUES ("NamBui"); 
UPDATE Employee SET Person = (SELECT Person_id FROM Person WHERE Person_ref = "NamBui") WHERE Employee_ref = "NamBui";
UPDATE Person SET name = "Nam" WHERE Person_ref = "NamBui";
UPDATE Person SET surname = "Bui" WHERE Person_ref = "NamBui";
UPDATE Person SET age = 30 WHERE Person_ref = "NamBui";

INSERT INTO Car (Car_ref) VALUES ("CityBlack");

INSERT INTO ownership (owners, ownedCars) VALUES (
(SELECT Person_id FROM Person WHERE Person_ref = "HoangNguyen") , 
(SELECT Car_id FROM Car WHERE Car_ref = "CityBlack") );


