/*
 * VGU
 */
CREATE DATABASE vguDB;
use vguDB;


DROP TABLE IF EXISTS role;
CREATE TABLE role(
  role_id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
  name varchar(100)
);

INSERT role(name)VALUES("Student");
INSERT role(name)VALUES("Lecturer");
INSERT role(name)VALUES("Staff");
INSERT role(name)VALUES("Admin");

DROP TABLE IF EXISTS reg_user;
CREATE TABLE reg_user(
  Reg_User_id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
  given_name varchar(100),
  middle_name varchar(100),
  family_name varchar(100),
  email varchar(100),
  role int NOT NULL,
  FOREIGN KEY(role) 
     REFERENCES role(role_id)
);


INSERT INTO reg_user(given_name, middle_name, family_name, email, role) 
   VALUES ("Trung", "Quốc", "Phạm", "trung@gmail.com",   1);

INSERT INTO reg_user(given_name, middle_name, family_name, email, role)  
   VALUES("Manuel", "Garcia", "Clavel", "manuel@gmail.com",  2);

INSERT INTO reg_user(given_name, middle_name, family_name, email, role)  
   VALUES("Jaime", "Nubiola", "Aguilar", "jaime@gmail.com",  2);

INSERT INTO reg_user(given_name, middle_name, family_name, email, role)  
   VALUES("Trang", "Thi Thuy", "Nguyen", "trang@gmail.com",  4);

INSERT INTO reg_user(given_name, family_name, email, role) 
   VALUES ("Aldiyar", "Zagitov Yerzhanovich", "aldiyar@gmail.com", 1);



DROP TABLE IF EXISTS Lecturer;
CREATE TABLE Lecturer(
  Lecturer_id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
  Reg_User int NOT NULL,
  FOREIGN KEY(Reg_User) 
     REFERENCES Reg_User(Reg_User_id)
);

INSERT INTO Lecturer (Reg_user) VALUES (2);

DROP TABLE IF EXISTS Student;
CREATE TABLE Student(
  Student_id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
  Reg_User int NOT NULL,
  FOREIGN KEY(Reg_User) 
     REFERENCES Reg_User(Reg_User_id)
);

INSERT INTO Student (Reg_user) VALUES (1);
INSERT INTO Student (Reg_user) VALUES (5);

DROP TABLE IF EXISTS Course;
CREATE TABLE Course (
  Course_id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
  name varchar(100)
) ENGINE=InnoDB;

INSERT INTO Course (name) VALUES ("Software Engineering Analysis");

DROP TABLE IF EXISTS Enrollment;
CREATE TABLE Enrollment (
  courses int,
  students int,
  FOREIGN KEY (courses) 
     REFERENCES Course(Course_id),
  FOREIGN KEY (students) 
     REFERENCES Student(Student_id)
) ENGINE=InnoDB;

INSERT INTO Enrollment (courses, students) VALUES (1, 1);

DROP TABLE IF EXISTS Teaching;
CREATE TABLE Teaching (
  courses int,
  lecturers int,
  FOREIGN KEY (courses) 
     REFERENCES Course(Course_id),
  FOREIGN KEY (lecturers) 
     REFERENCES Lecturer(Lecturer_id)
) ENGINE=InnoDB;


INSERT INTO Teaching (lecturers, courses) VALUES (1, 1);

DROP TABLE IF EXISTS RequestBy;
CREATE TABLE RequestBy (
 requested int,
  requestedBy int,
  FOREIGN KEY (requested) 
     REFERENCES FriendRequest(FriendRequest_id),
  FOREIGN KEY (requestedBy) 
     REFERENCES Reg_User(Reg_User_id)
) ENGINE=InnoDB;

DROP TABLE IF EXISTS RequestTo;
CREATE TABLE RequestTo (
 received int,
  requestedTo int,
  FOREIGN KEY (received) 
     REFERENCES FriendRequest(FriendRequest_id),
  FOREIGN KEY (requestedTo) 
     REFERENCES Reg_User(Reg_User_id)
) ENGINE=InnoDB;
