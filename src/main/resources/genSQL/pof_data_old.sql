USE unipof_old;

DELETE FROM Registration_Exam;

DELETE FROM Exam;

DELETE FROM Record;

DELETE FROM Enrollment;

DELETE FROM Module_Period;

DELETE FROM Module;

DELETE FROM Program;

DELETE FROM Student;

DELETE FROM Period;

ALTER TABLE Program AUTO_INCREMENT = 1;
INSERT INTO Program (name, code, doe) 
VALUES ("Computer Science", "CS", "20130101"), 
("Finance and Accounting", "FA", "20110101");

ALTER TABLE Module AUTO_INCREMENT = 1;
INSERT INTO Module (program, code, name)
VALUES (1, "CS001", "Mathematics"),
(1, "CS002", "Software-Engineering");

ALTER TABLE Period AUTO_INCREMENT = 1;
INSERT INTO Period (name, starts, ends)
VALUES ("WS19", "20190901", "20200303");

ALTER TABLE Module_Period AUTO_INCREMENT = 1;
INSERT INTO Module_Period (period, module, name, starts, ends)
VALUES (1, 2, "SWE-WS19", "20191201", "20191227"),
(1, 1, "Math-WS19", "20191001", "20191101");

ALTER TABLE Student AUTO_INCREMENT = 1;
INSERT INTO Student (code, gender, dob, midname, firstname, lastname, birthplace) 
VALUES ("1141818", "M", "19970715", NULL, "Nguyen", "Duy", "Vietnam"), 
("1150302", "F", "19950304", "Ngoc Thoai", "An", "Bui", "Vietnam");

ALTER TABLE Enrollment AUTO_INCREMENT = 1;
INSERT INTO Enrollment (student, program, starts, ends) 
VALUES (1,1,"20140901", NULL);

ALTER TABLE Record AUTO_INCREMENT = 1;
INSERT INTO Record (student, module_period, status)
VALUES (1, 1, "NG");

ALTER TABLE Exam AUTO_INCREMENT = 1;
INSERT INTO Exam (module_period, date, starts, ends, deadline)
VALUES  (1, "20191227", "10:00:00", "11:30:00", "20191224");

ALTER TABLE Registration_Exam AUTO_INCREMENT = 1;
INSERT INTO Registration_Exam (exam, record, datetime, status)
VALUES (1, 1, "2019-12-23 13:00:00", "R");



