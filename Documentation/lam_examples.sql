CREATE DATABASE lam_test;
use lam_test;


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
  gender char(1),
  data_birth DATE,
  role int NOT NULL,
  FOREIGN KEY(role) 
     REFERENCES role(role_id)
);


INSERT INTO reg_user(given_name, family_name,
   gender, role) VALUES ("Aldiyar", "Zagitov Yerzhanovich", "M", 3);

INSERT INTO reg_user(given_name, middle_name, family_name,
   gender, role) VALUES ("Trung", "Quốc", "Phạm", "M", 3);

INSERT INTO reg_user(given_name, middle_name, family_name, 
   gender, data_birth, role) VALUES("Manuel", "Garcia", "Clavel", "M", "1969-09-05", 2);

INSERT INTO reg_user(given_name, middle_name, family_name, 
   gender, data_birth, role) VALUES("Jaime", "Nubiola", "Aguilar", "M", "1959-09-05", 2);

INSERT INTO reg_user(given_name, middle_name, family_name, 
   gender, role) VALUES("Trang", "Thi Thuy", "Nguyen", "F", 1);

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
  course int,
  student int,
  FOREIGN KEY (course) 
     REFERENCES course(course_id),
  FOREIGN KEY (student) 
     REFERENCES student(student_id)
) ENGINE=InnoDB;


INSERT INTO course_student VALUES (1,1);


DROP TABLE IF EXISTS lecturer;
CREATE TABLE lecturer (
  lecturer_id int NOT NULL AUTO_INCREMENT  PRIMARY KEY,
  reg_user int,
  FOREIGN KEY (reg_user) 
     REFERENCES reg_user(reg_user_id)
) ENGINE=InnoDB;


INSERT INTO lecturer(reg_user) VALUES (2);


DROP TABLE IF EXISTS course_lecturer;
CREATE TABLE course_lecturer (
  course int,
  lecturer int,
  FOREIGN KEY (course) 
     REFERENCES course(course_id),
  FOREIGN KEY (lecturer) 
     REFERENCES lecturer(lecturer_id)
) ENGINE=InnoDB;


INSERT INTO course_lecturer VALUES (1,1);


DROP PROCEDURE IF EXISTS GetCourseStudentList;
DELIMITER //
CREATE PROCEDURE GetCourseStudentList(IN course_id INT, IN caller_id INT)
BEGIN
	SELECT family_name 
	FROM reg_user
	INNER JOIN 
	(SELECT reg_user FROM Student
	INNER JOIN
	(SELECT student FROM course_student
	WHERE course_student.course = course_id) AS TEMP_1
	ON student_id = TEMP_1.student) AS TEMP_2
	ON TEMP_2.reg_user = reg_user_id;
END //
DELIMITER ;

DROP PROCEDURE IF EXISTS GetLecturerCourseList;
DELIMITER //
CREATE PROCEDURE GetLecturerCourseList(IN lecturer_id INT, IN caller_id INT)
 BEGIN
    SELECT  name
  	FROM course
  	INNER JOIN 
  	(SELECT course FROM course_lecturer
  	WHERE course_lecturer.lecturer = lecturer_id) AS TEMP
    ON course_id = TEMP.course;
END //
DELIMITER ;


SELECT TEMP_8.item OR TEMP_9.item AS item INTO result 
FROM 
  (SELECT TEMP_2.item OR TEMP_7.item AS item 
   FROM  
     (SELECT TEMP_0.item = TEMP_1.item AS item 
      FROM (SELECT kself AS item) AS TEMP_0, 
           (SELECT kcaller AS item) AS TEMP_1)
     AS TEMP_2, 
     (SELECT TEMP_5.item = TEMP_6.item AS item 
     FROM 
       (SELECT emailPrivacy AS item  
        FROM (SELECT * FROM Reg_User) AS TEMP_4 
        RIGHT JOIN (SELECT kself AS item) AS TEMP_3 
        ON TEMP_4.Reg_User_id = TEMP_3.item) 
        AS TEMP_5, 
		(SELECT 'Public' AS item) AS TEMP_6)
  AS TEMP_7) 
AS TEMP_8, 
(SELECT (5) AS item) AS TEMP_9;


SELECT TEMP_13.item AND TEMP_18.item AS item INTO result 
FROM 
(SELECT TEMP_8.item OR TEMP_12.item AS item 
   FROM 
    (SELECT TEMP_2.item OR TEMP_7.item AS item FROM 
         (SELECT TEMP_0.item = TEMP_1.item AS item 
              FROM (SELECT kself AS item) AS TEMP_0, 
         (SELECT kcaller AS item) AS TEMP_1) AS TEMP_2, 
         (SELECT TEMP_5.item = TEMP_6.item AS item 
              FROM 
                 (SELECT emailPrivacy AS item 
                      FROM (SELECT * FROM Reg_User) AS TEMP_4 
                       RIGHT JOIN (SELECT kself AS item) AS TEMP_3 
                        ON TEMP_4.Reg_User_id = TEMP_3.item) AS TEMP_5, 
                        (SELECT 'Public' AS item) AS TEMP_6) AS TEMP_7) AS TEMP_8, 
                        (SELECT COUNT(TEMP_11.item) > 0 AS item 
                        FROM (SELECT myFriends AS item 
                        FROM (SELECT * FROM Reg_User_Reg_User) AS TEMP_10 
                        RIGHT JOIN (SELECT kcaller AS item) AS TEMP_9 
                        ON TEMP_10.friendOf = TEMP_9.item) 
                        AS TEMP_11) AS TEMP_12)
                 AS TEMP_13, 
     (SELECT TEMP_16.item = TEMP_17.item AS item FROM 
                        (SELECT emailPrivacy AS item 
                        FROM (SELECT * FROM Reg_User) AS TEMP_15 
                        RIGHT JOIN (SELECT kself AS item) AS TEMP_14 
                        ON TEMP_15.Reg_User_id = TEMP_14.item) AS TEMP_16, 
         (SELECT 'Friends' AS item) 
         AS TEMP_17) AS TEMP_18;


DROP PROCEDURE IF EXISTS GetLecturerCourseStudentList;
DELIMITER //
CREATE PROCEDURE GetLecturerCourseStudentList(IN lecturer_id INT, course_id INT, IN caller_id INT)
 BEGIN
	SELECT family_name 
	FROM reg_user
	INNER JOIN
   	(SELECT reg_user 
  	FROM student
  	INNER JOIN
  	(SELECT student FROM course_student
  	INNER JOIN 
  	(SELECT course FROM course_lecturer 
  	WHERE course = course_id AND lecturer = lecturer_id) AS TEMP_1
  	ON course_student.course = TEMP_1.course) as TEMP_2 
  	ON student_id = TEMP_2.student) AS TEMP_3
  	ON TEMP_3.reg_user = reg_user_id;
  	END //
DELIMITER ;

/* examples
 * CALL GetCourseStudentList(1, 1, 3)
 * CALL GetLecturerCourseList(1, 1, 3)
 * CALL GetLecturerCourseStudentList(1, 1, 3)
 */

