DROP DATABASE IF EXISTS unipof;
CREATE DATABASE unipof 
 DEFAULT CHARACTER SET utf8 
 DEFAULT COLLATE utf8_general_ci;
USE unipof;
CREATE TABLE Module_Period (Module_Period_id INT (11) NOT NULL AUTO_INCREMENT PRIMARY KEY, starts DATE NOT NULL, name VARCHAR (100) UNIQUE NOT NULL, ends DATE NOT NULL);
CREATE TABLE Program (Program_id INT (11) NOT NULL AUTO_INCREMENT PRIMARY KEY, name VARCHAR (100) NOT NULL UNIQUE, code VARCHAR (100) UNIQUE NOT NULL, doe DATE NOT NULL);
CREATE TABLE Registration_Exam (Registration_Exam_id INT (11) NOT NULL AUTO_INCREMENT PRIMARY KEY, datetime DATETIME NOT NULL, status VARCHAR (100) NOT NULL);
CREATE TABLE Enrollment (Enrollment_id INT (11) NOT NULL AUTO_INCREMENT PRIMARY KEY, ends DATE , starts DATE NOT NULL);
CREATE TABLE Period (Period_id INT (11) NOT NULL AUTO_INCREMENT PRIMARY KEY, name VARCHAR (100) , starts DATE NOT NULL, ends DATE NOT NULL);
CREATE TABLE Record (Record_id INT (11) NOT NULL AUTO_INCREMENT PRIMARY KEY, status VARCHAR (100) NOT NULL);
CREATE TABLE Student (Student_id INT (11) NOT NULL AUTO_INCREMENT PRIMARY KEY, firstname VARCHAR (100) NOT NULL, lastname VARCHAR (100) NOT NULL, birthplace VARCHAR (100) NOT NULL, code VARCHAR (100) UNIQUE NOT NULL, dob DATE NOT NULL, midname VARCHAR (100) , gender VARCHAR (100) NOT NULL);
CREATE TABLE Module (Module_id INT (11) NOT NULL AUTO_INCREMENT PRIMARY KEY, name VARCHAR (100) NOT NULL, code VARCHAR (100) UNIQUE NOT NULL);
CREATE TABLE Exam (Exam_id INT (11) NOT NULL AUTO_INCREMENT PRIMARY KEY, deadline DATETIME NOT NULL, starts TIME NOT NULL, date DATE NOT NULL, ends TIME NOT NULL);
ALTER TABLE Record ADD COLUMN student INT (11), ADD FOREIGN KEY (student) REFERENCES Student (Student_id);
ALTER TABLE Module_Period ADD COLUMN module INT (11), ADD FOREIGN KEY (module) REFERENCES Module (Module_id);
ALTER TABLE Enrollment ADD COLUMN program INT (11), ADD FOREIGN KEY (program) REFERENCES Program (Program_id);
ALTER TABLE Registration_Exam ADD COLUMN exam INT (11), ADD FOREIGN KEY (exam) REFERENCES Exam (Exam_id);
ALTER TABLE Enrollment ADD COLUMN student INT (11), ADD FOREIGN KEY (student) REFERENCES Student (Student_id);
ALTER TABLE Module_Period ADD COLUMN period INT (11), ADD FOREIGN KEY (period) REFERENCES Period (Period_id);
ALTER TABLE Module ADD COLUMN program INT (11), ADD FOREIGN KEY (program) REFERENCES Program (Program_id);
ALTER TABLE Registration_Exam ADD COLUMN record INT (11), ADD FOREIGN KEY (record) REFERENCES Record (Record_id);
ALTER TABLE Exam ADD COLUMN module_period INT (11), ADD FOREIGN KEY (module_period) REFERENCES Module_Period (Module_Period_id);
ALTER TABLE Record ADD COLUMN module_period INT (11), ADD FOREIGN KEY (module_period) REFERENCES Module_Period (Module_Period_id);
DROP FUNCTION IF EXISTS valid_student_age_at_least_18;
DELIMITER //
CREATE FUNCTION valid_student_age_at_least_18 ()
RETURNS INT DETERMINISTIC
BEGIN
DECLARE result INT DEFAULT 0;
SELECT res INTO result FROM (SELECT 1 AS val, COUNT(*) > 0 AS res
FROM (SELECT TEMP_body.val AS val, TEMP_body.res AS res
FROM (SELECT Student.dob AS res, 1 AS val, Student.Student_id AS ref_s
FROM Student) AS TEMP_body) AS TEMP_SRC) AS TEMP_result;
RETURN (result);
END //
DELIMITER ;
DROP FUNCTION IF EXISTS valid_program_doe_before_today;
DELIMITER //
CREATE FUNCTION valid_program_doe_before_today ()
RETURNS INT DETERMINISTIC
BEGIN
DECLARE result INT DEFAULT 0;
SELECT res INTO result FROM (SELECT 1 AS val, COUNT(*) = 0 AS res
FROM (SELECT TEMP_LEFT.res < TEMP_RIGHT.res AS res, CASE TEMP_LEFT.val = 0 OR TEMP_RIGHT.val = 0 WHEN 1 THEN 0 ELSE 1 END AS val, TEMP_LEFT.ref_p AS ref_p
FROM (SELECT Program.doe AS res, 1 AS val, Program.Program_id AS ref_p
FROM Program) AS TEMP_LEFT
JOIN (SELECT 1 AS val, CURDATE() AS res) AS TEMP_RIGHT) AS TEMP_BODY
WHERE TEMP_BODY.res = 0) AS TEMP_result;
RETURN (result);
END //
DELIMITER ;
DROP FUNCTION IF EXISTS valid_enrollment_starts_before_ends;
DELIMITER //
CREATE FUNCTION valid_enrollment_starts_before_ends ()
RETURNS INT DETERMINISTIC
BEGIN
DECLARE result INT DEFAULT 0;
SELECT res INTO result FROM (SELECT 1 AS val, COUNT(*) = 0 AS res
FROM (SELECT TEMP_LEFT.res OR TEMP_RIGHT.res AS res, CASE TEMP_LEFT.val = 0 OR TEMP_RIGHT.val = 0 WHEN 1 THEN 0 ELSE 1 END AS val, TEMP_LEFT.ref_e AS ref_e
FROM (SELECT TEMP_SOURCE.val AS val, CASE TEMP_SOURCE.val = 0 WHEN 1 THEN NULL ELSE TEMP_SOURCE.res IS NULL END AS res, TEMP_SOURCE.ref_e AS ref_e
FROM (SELECT Enrollment.ends AS res, 1 AS val, Enrollment.Enrollment_id AS ref_e
FROM Enrollment) AS TEMP_SOURCE) AS TEMP_LEFT
LEFT JOIN (SELECT TEMP_LEFT.res > TEMP_RIGHT.res AS res, CASE TEMP_LEFT.val = 0 OR TEMP_RIGHT.val = 0 WHEN 1 THEN 0 ELSE 1 END AS val, TEMP_LEFT.ref_e AS ref_e
FROM (SELECT Enrollment.ends AS res, 1 AS val, Enrollment.Enrollment_id AS ref_e
FROM Enrollment) AS TEMP_LEFT
LEFT JOIN (SELECT Enrollment.starts AS res, 1 AS val, Enrollment.Enrollment_id AS ref_e
FROM Enrollment) AS TEMP_RIGHT ON TEMP_LEFT.ref_e = TEMP_RIGHT.ref_e) AS TEMP_RIGHT ON TEMP_LEFT.ref_e = TEMP_RIGHT.ref_e) AS TEMP_BODY
WHERE TEMP_BODY.res = 0) AS TEMP_result;
RETURN (result);
END //
DELIMITER ;
DROP FUNCTION IF EXISTS valid_enrollment_starts_after_program_doe;
DELIMITER //
CREATE FUNCTION valid_enrollment_starts_after_program_doe ()
RETURNS INT DETERMINISTIC
BEGIN
DECLARE result INT DEFAULT 0;
SELECT res INTO result FROM (SELECT 1 AS val, COUNT(*) = 0 AS res
FROM (SELECT TEMP_LEFT.res < TEMP_RIGHT.res AS res, CASE TEMP_LEFT.val = 0 OR TEMP_RIGHT.val = 0 WHEN 1 THEN 0 ELSE 1 END AS val, TEMP_LEFT.ref_e AS ref_e
FROM (SELECT Enrollment.starts AS res, 1 AS val, Enrollment.Enrollment_id AS ref_e
FROM Enrollment) AS TEMP_LEFT
LEFT JOIN (SELECT TEMP_OBJ.val AS val, Program.doe AS res, TEMP_OBJ.ref_e AS ref_e
FROM (SELECT Enrollment.program AS res, Enrollment.Enrollment_id AS ref_e
FROM Enrollment) AS TEMP_OBJ
LEFT JOIN Program ON TEMP_OBJ.ref_e = Program.Program_id AND TEMP_OBJ.val = 1) AS TEMP_RIGHT ON TEMP_LEFT.ref_e = TEMP_RIGHT.ref_e) AS TEMP_BODY
WHERE TEMP_BODY.res = 0) AS TEMP_result;
RETURN (result);
END //
DELIMITER ;
DROP FUNCTION IF EXISTS valid_module_name_unique_by_program;
DELIMITER //
CREATE FUNCTION valid_module_name_unique_by_program ()
RETURNS INT DETERMINISTIC
BEGIN
DECLARE result INT DEFAULT 0;
SELECT res INTO result FROM (SELECT 1 AS val, COUNT(*) = 0 AS res
FROM (SELECT 1 AS val, CASE TEMP_SRC.val = 0 WHEN 1 THEN 1 ELSE COUNT(TEMP_SRC.res) = COUNT(DISTINCT TEMP_SRC.res) END AS res, TEMP_SRC.ref_p AS ref_p
FROM (SELECT TEMP_body.val AS val, TEMP_body.res AS res, TEMP_body.ref_p AS ref_p
FROM (SELECT TEMP_OBJ.val AS val, Module.name AS res, TEMP_OBJ.ref_m AS ref_m, TEMP_OBJ.ref_p AS ref_p
FROM (SELECT TEMP_DMN.val AS val, TEMP_DMN.res AS res, TEMP_DMN.res AS ref_m, TEMP_DMN.ref_p AS ref_p
FROM (SELECT CASE Program.program IS NULL WHEN 1 THEN 0 ELSE 1 END AS val, Program.Program_id AS res, Program.Program_id AS ref_p
FROM Module
LEFT JOIN Program ON Program.Program_id = Module.program) AS TEMP_DMN) AS TEMP_OBJ
LEFT JOIN Module ON TEMP_OBJ.ref_m = Module.Module_id AND TEMP_OBJ.val = 1) AS TEMP_body) AS TEMP_SRC
GROUP BY TEMP_SRC.ref_p, TEMP_SRC.val) AS TEMP_BODY
WHERE TEMP_BODY.res = 0) AS TEMP_result;
RETURN (result);
END //
DELIMITER ;
DROP FUNCTION IF EXISTS valid_record_no_duplicate_student_moduleperiod;
DELIMITER //
CREATE FUNCTION valid_record_no_duplicate_student_moduleperiod ()
RETURNS INT DETERMINISTIC
BEGIN
DECLARE result INT DEFAULT 0;
SELECT res INTO result FROM (SELECT 1 AS val, COUNT(*) = 0 AS res
FROM (SELECT 1 AS val, Record_id AS res
FROM Record) AS TEMP_SOURCE
JOIN (SELECT 1 AS val, COUNT(*) = 0 AS res
FROM (SELECT TEMP_LEFT.res OR TEMP_RIGHT.res AS res, CASE TEMP_LEFT.val = 0 OR TEMP_RIGHT.val = 0 WHEN 1 THEN 0 ELSE 1 END AS val, TEMP_LEFT.ref_r1 AS ref_r1, TEMP_LEFT.ref_r2 AS ref_r2
FROM (SELECT TEMP_LEFT.res = TEMP_RIGHT.res AS res, CASE TEMP_LEFT.val = 0 OR TEMP_RIGHT.val = 0 WHEN 1 THEN 0 ELSE 1 END AS val, TEMP_LEFT.ref_r2 AS ref_r2, TEMP_RIGHT.ref_r1 AS ref_r1
FROM (SELECT TEMP_DMN.val AS val, TEMP_DMN.res AS res, TEMP_DMN.res AS ref_r2
FROM (SELECT 1 AS val, Record_id AS res
FROM Record) AS TEMP_DMN) AS TEMP_LEFT
JOIN (SELECT TEMP_DMN.val AS val, TEMP_DMN.res AS res, TEMP_DMN.res AS ref_r1
FROM (SELECT 1 AS val, Record_id AS res
FROM Record) AS TEMP_DMN) AS TEMP_RIGHT) AS TEMP_LEFT
LEFT JOIN (SELECT TEMP_LEFT.res <> TEMP_RIGHT.res AS res, CASE TEMP_LEFT.val = 0 OR TEMP_RIGHT.val = 0 WHEN 1 THEN 0 ELSE 1 END AS val, TEMP_LEFT.ref_r2 AS ref_r2, TEMP_RIGHT.ref_r1 AS ref_r1
FROM (SELECT Record.module_period AS res, Record.Record_id AS ref_r2
FROM Record) AS TEMP_LEFT
JOIN (SELECT Record.module_period AS res, Record.Record_id AS ref_r1
FROM Record) AS TEMP_RIGHT) AS TEMP_RIGHT ON TEMP_LEFT.ref_r2 = TEMP_RIGHT.ref_r2) AS TEMP_BODY
WHERE TEMP_BODY.res = 0) AS TEMP_BODY
WHERE TEMP_BODY.res = 0) AS TEMP_result;
RETURN (result);
END //
DELIMITER ;
