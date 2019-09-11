/*
 * SOCIAL
 */
CREATE DATABASE socialDB;
use socialDB;


DROP TABLE IF EXISTS role;
CREATE TABLE role(
  role_id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
  name varchar(100)
);

INSERT role(name)VALUES("Reg_User");
INSERT role(name)VALUES("Admin");
INSERT role(name)VALUES("Default");

DROP TABLE IF EXISTS reg_user;
CREATE TABLE reg_user(
  Reg_User_id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
  given_name varchar(100),
  middle_name varchar(100),
  family_name varchar(100),
  email varchar(100),
  emailPrivacy varchar(100),
  role int NOT NULL,
  FOREIGN KEY(role) 
     REFERENCES role(role_id)
);

INSERT INTO reg_user(given_name, family_name, email, emailPrivacy, role) 
   VALUES ("Aldiyar", "Zagitov Yerzhanovich", "aldiyar@gmail.com", "Friends", 1);

INSERT INTO reg_user(given_name, middle_name, family_name, email, emailPrivacy, role) 
   VALUES ("Trung", "Quốc", "Phạm", "trung@gmail.com", "Friends",  1);

INSERT INTO reg_user(given_name, middle_name, family_name, email, emailPrivacy, role)  
   VALUES("Manuel", "Garcia", "Clavel", "manuel@gmail.com", "OnlyMe", 1);

INSERT INTO reg_user(given_name, middle_name, family_name, email, emailPrivacy, role)  
   VALUES("Jaime", "Nubiola", "Aguilar", "jaime@gmail.com", "Public", 1);

INSERT INTO reg_user(given_name, middle_name, family_name, email, emailPrivacy, role)  
   VALUES("Trang", "Thi Thuy", "Nguyen", "trang@gmail.com", "OnlyMe", 3);


DROP TABLE IF EXISTS Friendship;
CREATE TABLE Friendship (
  myFriends int,
  friendOf int,
  FOREIGN KEY (myFriends) 
     REFERENCES Reg_User(Reg_User_id),
  FOREIGN KEY (friendOf) 
     REFERENCES Reg_User(Reg_User_id)
) ENGINE=InnoDB;


INSERT INTO Friendship (myFriends, friendOf) VALUES (1,2);

DROP TABLE IF EXISTS FriendRequest;
CREATE TABLE FriendRequest (
 FriendRequest_id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
  status int
) ENGINE=InnoDB;



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



/*
 * Database
 */
CREATE DATABASE programDB;
use programDB;


DROP TABLE IF EXISTS role;
CREATE TABLE role(
  role_id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
  name varchar(100)
);

INSERT role(name)VALUES("Admin"), ("Lecturer"), ("Student");

DROP TABLE IF EXISTS reg_user;
CREATE TABLE reg_user(
  reg_user_id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
  given_name varchar(100),
  middle_name varchar(100),
  family_name varchar(100),
  email varchar(100),
  emailPrivacy varchar(100),
  role int NOT NULL,
  FOREIGN KEY(role) 
     REFERENCES role(role_id)
);


INSERT INTO reg_user(given_name, family_name, role) VALUES ("Aldiyar", "Zagitov Yerzhanovich", 3);

INSERT INTO reg_user(given_name, middle_name, family_name, role) VALUES ("Trung", "Quốc", "Phạm", 3);

INSERT INTO reg_user(given_name, middle_name, family_name, role) VALUES("Manuel", "Garcia", "Clavel", 2);

INSERT INTO reg_user(given_name, middle_name, family_name, role) VALUES("Jaime", "Nubiola", "Aguilar", 2);

INSERT INTO reg_user(given_name, middle_name, family_name, role) VALUES("Trang", "Thi Thuy", "Nguyen", 1);

DROP TABLE IF EXISTS course;
CREATE TABLE course (
  course_id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
  name varchar(100)
) ENGINE=InnoDB;

INSERT course(name)VALUES("System Architecture");
INSERT course(name)VALUES("Distributed Systems");
INSERT course(name)VALUES("Software Engineering");


DROP TABLE IF EXISTS student;
CREATE TABLE student (
  student_id int NOT NULL AUTO_INCREMENT  PRIMARY KEY,
  reg_user int,
  FOREIGN KEY (reg_user) 
     REFERENCES reg_user(reg_user_id)
) ENGINE=InnoDB;

INSERT INTO student(reg_user) VALUES (1);
INSERT INTO student(reg_user) VALUES (3);


DROP TABLE IF EXISTS course_student;
CREATE TABLE course_student (
 course_student_id int NOT NULL AUTO_INCREMENT  PRIMARY KEY,
  courses int,
  students int,
  FOREIGN KEY (courses) 
     REFERENCES course(course_id),
  FOREIGN KEY (students) 
     REFERENCES student(student_id)
) ENGINE=InnoDB;


INSERT INTO course_student (courses, students)VALUES (1,1);
INSERT INTO course_student (courses, students)VALUES (2,1);

DROP TABLE IF EXISTS lecturer;
CREATE TABLE lecturer (
  lecturer_id int NOT NULL AUTO_INCREMENT  PRIMARY KEY,
  reg_user int,
  FOREIGN KEY (reg_user) 
     REFERENCES reg_user(reg_user_id)
) ENGINE=InnoDB;


INSERT INTO lecturer(reg_user) VALUES (2);
INSERT INTO lecturer(reg_user) VALUES (5);

DROP TABLE IF EXISTS course_lecturer;
CREATE TABLE course_lecturer (
 course_lecturer_id int NOT NULL AUTO_INCREMENT  PRIMARY KEY,
  courses int,
  lecturers int,
  FOREIGN KEY (courses) 
     REFERENCES course(course_id),
  FOREIGN KEY (lecturers) 
     REFERENCES lecturer(lecturer_id)
) ENGINE=InnoDB;

INSERT INTO course_lecturer (courses, lecturers) VALUES (1,1);

/*** ADDING COORDINATORS ***/
INSERT role(name)VALUES("Coordinator");
INSERT INTO reg_user(given_name, family_name, role) VALUES ("Michel", "Toulouse", 4);

DROP TABLE IF EXISTS coordinator;
CREATE TABLE coordinator (
  coordinator_id int NOT NULL AUTO_INCREMENT  PRIMARY KEY,
  reg_user int,
  FOREIGN KEY (reg_user) 
     REFERENCES reg_user(reg_user_id)
) ENGINE=InnoDB;

INSERT INTO coordinator(reg_user) VALUES (7);


/*** ADDING PROGRAMS ***/
DROP TABLE IF EXISTS program;
CREATE TABLE program (
  program_id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
  name varchar(100)
) ENGINE=InnoDB;

INSERT program(name)VALUES("Computer Science");
INSERT program(name)VALUES("Business and Information Systems");


DROP TABLE IF EXISTS course_program;
CREATE TABLE course_program (
 course_program_id int NOT NULL AUTO_INCREMENT  PRIMARY KEY,
  courses int,
  programs int,
  FOREIGN KEY (courses) 
     REFERENCES course(course_id),
  FOREIGN KEY (programs) 
     REFERENCES program(program_id)
) ENGINE=InnoDB;

INSERT INTO course_program (courses, programs) VALUES (3,1);

DROP TABLE IF EXISTS coordinator_program;
CREATE TABLE coordinator_program (
 coordinator_program_id int NOT NULL AUTO_INCREMENT  PRIMARY KEY,
  coordinators int,
  programs int,
  FOREIGN KEY (coordinators) 
     REFERENCES coordinator(coordinator_id),
  FOREIGN KEY (programs) 
     REFERENCES program(program_id)
) ENGINE=InnoDB;

INSERT INTO coordinator_program (coordinators, programs) VALUES (1,1);





/*
 * Queries
 */

/*
 * List of students (family_name, given_name) registered in the program
 * 
 * OCL: reg_user.allInstances()->select(u|u.oclIsType(student))
 */
DROP PROCEDURE IF EXISTS GetStudentListA;
DELIMITER //
CREATE PROCEDURE GetStudentListA()
BEGIN
SELECT family_name, middle_name, given_name FROM reg_user
INNER JOIN student
ON reg_user_id = student_id;
END //
DELIMITER ;


DROP FUNCTION IF EXISTS auth_read_name_lecturer;
DELIMITER //
CREATE FUNCTION auth_read_name_lecturer(self_id int, caller_id int)
RETURNS INT DETERMINISTIC
BEGIN
DECLARE result INT DEFAULT 0;
SELECT count(*)> 0 into result FROM course_student
INNER JOIN 
(SELECT * FROM course_lecturer
INNER JOIN lecturer
on lecturer = lecturer_id
WHERE lecturer.reg_user = caller_id) AS TEMP0
ON course_student.course = TEMP0.course
WHERE course_student.student = self_id;
RETURN (result);
END //
DELIMITER ;

DROP FUNCTION IF EXISTS auth_read_name_student;
DELIMITER //
CREATE FUNCTION auth_read_name_student(self_id int, caller_id int)
RETURNS INT DETERMINISTIC
BEGIN
DECLARE result INT DEFAULT 0;
SELECT count(*)> 0 into result FROM course_student
INNER JOIN 
(SELECT * FROM course_student
INNER JOIN student
on student = student_id
WHERE student.reg_user = caller_id) AS TEMP0
ON course_student.course = TEMP0.course
WHERE course_student.student = self_id;
RETURN (result);
END //
DELIMITER ;

DROP FUNCTION IF EXISTS auth_read_name_admin;
DELIMITER //
CREATE FUNCTION auth_read_name_admin(self_id int, caller_id int)
RETURNS INT DETERMINISTIC
BEGIN
RETURN (1);
END //
DELIMITER ;


DROP FUNCTION IF EXISTS auth_read_name;
DELIMITER //
CREATE FUNCTION auth_read_name(self_id int, caller_id int, caller_role int)
RETURNS INT DETERMINISTIC
BEGIN
IF (caller_role = 2) THEN 
  IF (auth_read_name_lecturer(self_id, caller_id)) THEN 
    RETURN (1);
  ELSE 
    SIGNAL SQLSTATE '45000'
    SET MESSAGE_TEXT = 'Security exception'; 
    RETURN (0);
  END IF;
ELSE 
IF (caller_role = 3) THEN 
  IF (auth_read_name_student(self_id, caller_id)) THEN 
    RETURN (1);
  ELSE 
    SIGNAL SQLSTATE '45000'
    SET MESSAGE_TEXT = 'Security exception'; 
    RETURN (0);
  END IF;
ELSE 
IF (caller_role = 1) THEN
  IF (auth_read_name_admin(self_id, caller_id)) THEN 
    RETURN (1);
  ELSE 
    SIGNAL SQLSTATE '45000'
    SET MESSAGE_TEXT = 'Security exception'; 
    RETURN (0);
  END IF;
ELSE  
SIGNAL SQLSTATE '45000'
  SET MESSAGE_TEXT = 'Security exception'; 
  RETURN (0);
END IF;
END IF;
END IF;
END //
DELIMITER ;


DROP PROCEDURE IF EXISTS GetStudentListSec;
 DELIMITER //
 CREATE PROCEDURE GetStudentListSec(IN caller_id INT)
 BEGIN
    DECLARE caller_role INT DEFAULT 0;
    SELECT role INTO caller_role FROM reg_user WHERE reg_user_id = caller_id;
    SELECT CASE auth_read_name(reg_user_id, caller_id, caller_role)
	          WHEN 1 THEN reg_user.family_name END AS family_name,
	       CASE auth_read_name(reg_user_id, caller_id, caller_role)
	          WHEN 1 THEN reg_user.middle_name END AS middle_name,
	       CASE auth_read_name(reg_user_id, caller_id, caller_role)
	          WHEN 1 THEN reg_user.given_name END AS given_name
  	FROM Student
  	INNER JOIN Reg_User
  	on reg_user = reg_user_id;
    END //
DELIMITER ;

/*
 * List of students (family_name, given_name) enrolled in a <course>
 * 
 * OCL: <course>.students.reg_user
 */

DROP PROCEDURE IF EXISTS GetCourseStudentListA;
DELIMITER //
CREATE PROCEDURE GetCourseStudentListA(IN course_id INT)
BEGIN
SELECT family_name, given_name FROM reg_user
INNER JOIN 
(SELECT reg_user FROM course_student
INNER JOIN student
on course_student.student = student_id
WHERE course = course_id) AS TEMP
ON reg_user_id = TEMP.reg_user;
END //
DELIMITER ;

/*
 * NOTE: with this "transformation", when there are no students
 * in a course... a lecturer would not it, even if it is not
 * one of his/her courses!
 */
DROP PROCEDURE IF EXISTS GetCourseStudentListA;
 DELIMITER //
 CREATE PROCEDURE GetCourseStudentListA(IN course_id INT, IN caller_id INT)
 BEGIN
    DECLARE caller_role INT DEFAULT 0;
    SELECT role INTO caller_role FROM reg_user WHERE id = caller_id;
  	SELECT CASE auth_read_family_name(reg_user.id, caller_id, caller_role)
	WHEN 1 THEN reg_user.family_name END AS Family_Name 
  	FROM reg_user
  	INNER JOIN 
      (SELECT student_id FROM course_student
        WHERE course_student.course_id = course_id) AS TEMP
    ON reg_user.id = TEMP.student_id;
    END //
DELIMITER ;

/*
 * NOTE: 
 */
DROP FUNCTION IF EXISTS auth_read_student_id_lecturer;
DELIMITER //
CREATE FUNCTION auth_read_student_id_lecturer(self_id int, caller_id int)
RETURNS INT DETERMINISTIC
BEGIN
DECLARE result INT DEFAULT 0;
SELECT count(*)> 0 INTO result FROM course_lecturer
WHERE course_lecturer.lecturer_id = caller_id
AND course_lecturer.course_id = self_id;	
RETURN (result);
END //
DELIMITER ;

DECLARE result INT DEFAULT 0;
SELECT count(*)> 0 INTO result FROM course_lecturer
WHERE course_lecturer.lecturer_id = caller_id
AND course_lecture.course_id = self_id;




DROP FUNCTION IF EXISTS auth_read_student_id;
DELIMITER //
CREATE FUNCTION auth_read_student_id(self_id int, caller_id int, caller_role int)
RETURNS INT DETERMINISTIC
BEGIN
	DECLARE result INT DEFAULT 0;
IF (caller_role = 3) THEN 
SELECT auth_read_student_id_lecturer(self_id, caller_id) INTO result; 
IF (result) THEN 
  RETURN (1);
ELSE 
  SIGNAL SQLSTATE '45000'
  SET MESSAGE_TEXT = 'Security exception'; 
  RETURN (0);
END IF;
ELSE 
SIGNAL SQLSTATE '45000'
  SET MESSAGE_TEXT = 'Security exception'; 
  RETURN (0);
END IF;
END //
DELIMITER ;


DROP PROCEDURE IF EXISTS GetCourseStudentListB;
 DELIMITER //
 CREATE PROCEDURE GetCourseStudentListB(IN course_id INT, IN caller_id INT)
 BEGIN
    DECLARE caller_role INT DEFAULT 0;
    SELECT role INTO caller_role FROM reg_user WHERE id = caller_id;
  	SELECT CASE auth_read_name(reg_user.id, caller_id, caller_role)
	WHEN 1 THEN reg_user.family_name END AS family_name 
  	FROM reg_user
  	INNER JOIN 
      (SELECT CASE auth_read_student_id(course_student.course_id, caller_id, caller_role) 
        WHEN 1 THEN course_student.student_id END AS student_id 
        FROM course_student
        WHERE course_student.course_id = course_id) AS TEMP
    ON reg_user.id = TEMP.student_id;
    END //
DELIMITER ;

/* exists */
	SELECT count(*) > 0 FROM (SELECT * FROM Reg_User WHERE given_name = 'Manuel') AS TEMP;	
	
	SELECT count(*) > 0 FROM 
	(SELECT count(*) > 2 FROM  
	 (SELECT count(*) FROM course_lecturer 
	   INNER JOIN Lecturer
	   ON Lecturer.lecturer_id = course_lecturer.lecturer) AS TEMP0) AS TEMP1;	
	   
	  
	   /* Lecturer.allInstances->collect(x|x.courses->notEmpty) */
	   SELECT * FROM 
	   (SELECT count(*) > 0 
	   FROM course_lecturer 
	   INNER JOIN Lecturer
	   ON course_lecturer.lecturer = Lecturer.lecturer_id
	   GROUP BY lecturer) AS TEMP;
   
	   /* Reg_User.allInstances()->collect(x|x.given_name) */
	   SELECT * FROM (SELECT given_name FROM reg_user) AS COL1;
	   
