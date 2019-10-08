CREATE DATABASE genDB;
USE genDB;

CREATE TABLE Vehicle (
	Vehicle_id int(11) NOT NULL UNIQUE,
	category varchar(255) DEFAULT NULL,
	PRIMARY KEY (Vehicle_id)
) ENGINE=InnoDB;

CREATE TABLE Automobile (
	Automobile_id int(11) NOT NULL UNIQUE,
	price int(11) DEFAULT NULL,
	PRIMARY KEY (Automobile_id),
	CONSTRAINT vehicle_ibfk_1 
	FOREIGN KEY (Automobile_id) REFERENCES Vehicle (Vehicle_id)
) ENGINE=InnoDB;

CREATE TABLE Car (
	Car_id int(11) NOT NULL UNIQUE,
	color varchar(255) DEFAULT NULL,
	PRIMARY KEY (Car_id),
	CONSTRAINT automobile_ibfk_car
	FOREIGN KEY (Car_id) REFERENCES Automobile (Automobile_id)
) ENGINE=InnoDB;

CREATE TABLE Bus (
	Bus_id int(11) NOT NULL UNIQUE,
	color varchar(255) DEFAULT NULL,
	PRIMARY KEY (Bus_id),
	CONSTRAINT automobile_ibfk_bus
	FOREIGN KEY (Bus_id) REFERENCES Automobile (Automobile_id)
) ENGINE=InnoDB;

CREATE TABLE Person (
	Person_id int(11) NOT NULL UNIQUE,
	name varchar(255) DEFAULT NULL,
	PRIMARY KEY (Person_id)
) ENGINE=InnoDB;

CREATE TABLE Ownership (
	ownedCars int(11) DEFAULT NULL,
	owners int(11) DEFAULT NULL,
	KEY fk_ownership_ownedCars (ownedCars),
	KEY fk_ownership_owners (owners),
	CONSTRAINT ownership_ibfk_1 
	FOREIGN KEY (ownedCars) REFERENCES Car (Car_id),
	CONSTRAINT ownership_ibfk_2 
	FOREIGN KEY (owners) REFERENCES Person (Person_id)
) ENGINE=InnoDB;

INSERT INTO Vehicle values (1,'M'), (2,'M'), (3,'M'), (4,'M'), (5,'M');

INSERT INTO Automobile values (1, 100000), (2, 200000), (3, 300000), (4, 400000), (5, 500000);

INSERT INTO Car values (1, 'Black'), (2, 'Blue'), (3, 'White'), (4, NULL);

INSERT INTO Bus values (5, 'Red');

INSERT INTO Person values (1, 'Manuel'), (2, 'An'), (3, 'Hoang'), (4, NULL);

INSERT INTO Ownership (ownedCars, owners) values (1,1), (1,2), (3,4), (2,3);


