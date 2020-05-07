USE unipof;

SET FOREIGN_KEY_CHECKS=0;

DELETE FROM Account;
DELETE FROM Assistant;
DELETE FROM Enrollment;
DELETE FROM Exam;
DELETE FROM Exam_exams_rooms_Room;
DELETE FROM Lecturer;
DELETE FROM Module;
DELETE FROM Module_Group;
DELETE FROM Module_Period;
DELETE FROM Module_modules_prerequisites_Module;
DELETE FROM Period;
DELETE FROM Program;
DELETE FROM Record;
DELETE FROM Record_records_sessions_Session;
DELETE FROM Registration_Exam;
DELETE FROM Role;
DELETE FROM Room;
DELETE FROM Session;
DELETE FROM Student;
DELETE FROM University;

--
-- Dumping data for table `Account`
--

LOCK TABLES `Account` WRITE;
/*!40000 ALTER TABLE `Account` DISABLE KEYS */;
INSERT INTO `Account` VALUES (1,'manu','manu',NULL,3,1,NULL),(2,'trang','trang',NULL,2,NULL,1),(3,'hoang','hoang',3,1,NULL,NULL);
/*!40000 ALTER TABLE `Account` ENABLE KEYS */;
UNLOCK TABLES;
--
-- Dumping data for table `Assistant`
--

LOCK TABLES `Assistant` WRITE;
/*!40000 ALTER TABLE `Assistant` DISABLE KEYS */;
INSERT INTO `Assistant` VALUES (1,'Trang','Nguyen','trang@vgu.local','Thi Thu',2);
/*!40000 ALTER TABLE `Assistant` ENABLE KEYS */;
UNLOCK TABLES;
--
-- Dumping data for table `Enrollment`
--

LOCK TABLES `Enrollment` WRITE;
/*!40000 ALTER TABLE `Enrollment` DISABLE KEYS */;
INSERT INTO `Enrollment` VALUES (1,NULL,'2014-09-01',1,1);
/*!40000 ALTER TABLE `Enrollment` ENABLE KEYS */;
UNLOCK TABLES;
--
-- Dumping data for table `Exam`
--

LOCK TABLES `Exam` WRITE;
/*!40000 ALTER TABLE `Exam` DISABLE KEYS */;
INSERT INTO `Exam` VALUES (1,'2019-12-24 00:00:00','11:30:00','10:00:00','2019-12-27',1),(2,'2020-01-28 00:00:00','14:30:00','13:00:00','2020-02-03',2);
/*!40000 ALTER TABLE `Exam` ENABLE KEYS */;
UNLOCK TABLES;
--
-- Dumping data for table `Exam_exams_rooms_Room`
--

LOCK TABLES `Exam_exams_rooms_Room` WRITE;
/*!40000 ALTER TABLE `Exam_exams_rooms_Room` DISABLE KEYS */;
INSERT INTO `Exam_exams_rooms_Room` VALUES (1,1),(2,2);
/*!40000 ALTER TABLE `Exam_exams_rooms_Room` ENABLE KEYS */;
UNLOCK TABLES;
--
-- Dumping data for table `Lecturer`
--

LOCK TABLES `Lecturer` WRITE;
/*!40000 ALTER TABLE `Lecturer` DISABLE KEYS */;
INSERT INTO `Lecturer` VALUES (1,'Clavel','Garcia','Manu',1),(2,'','Toulouse','Michel',NULL);
/*!40000 ALTER TABLE `Lecturer` ENABLE KEYS */;
UNLOCK TABLES;
--
-- Dumping data for table `Module`
--

LOCK TABLES `Module` WRITE;
/*!40000 ALTER TABLE `Module` DISABLE KEYS */;
INSERT INTO `Module` VALUES (1,'CS001','Mathematics',1,1),(2,'CS002','Software-Engineering',1,2),(3,'CS003','Algebra',1,1);
/*!40000 ALTER TABLE `Module` ENABLE KEYS */;
UNLOCK TABLES;
--
-- Dumping data for table `Module_Group`
--

-- LOCK TABLES `Module_Group` WRITE;
/*!40000 ALTER TABLE `Module_Group` DISABLE KEYS */;
INSERT INTO `Module_Group` VALUES (1,'FY','FoundationYear'),(2,'OPY','OperationYear');
/*!40000 ALTER TABLE `Module_Group` ENABLE KEYS */;
-- UNLOCK TABLES;
--
-- Dumping data for table `Module_Period`
--

LOCK TABLES `Module_Period` WRITE;
/*!40000 ALTER TABLE `Module_Period` DISABLE KEYS */;
INSERT INTO `Module_Period` VALUES (1,'2019-12-27','SWE-WS19','2019-12-01',1,2,1),(2,'2019-11-01','Math-WS19','2019-10-01',NULL,1,1);
/*!40000 ALTER TABLE `Module_Period` ENABLE KEYS */;
UNLOCK TABLES;
--
-- Dumping data for table `Module_modules_prerequisites_Module`
--

LOCK TABLES `Module_modules_prerequisites_Module` WRITE;
/*!40000 ALTER TABLE `Module_modules_prerequisites_Module` DISABLE KEYS
 * */;
 INSERT INTO `Module_modules_prerequisites_Module` VALUES (1,2),(2,1);
/*!40000 ALTER TABLE `Module_modules_prerequisites_Module` ENABLE KEYS
 * */;
 UNLOCK TABLES;
--
-- Dumping data for table `Period`
--

LOCK TABLES `Period` WRITE;
/*!40000 ALTER TABLE `Period` DISABLE KEYS */;
INSERT INTO `Period` VALUES (1,'2020-03-03','WS19','2019-09-01');
/*!40000 ALTER TABLE `Period` ENABLE KEYS */;
UNLOCK TABLES;
--
-- Dumping data for table `Program`
--

LOCK TABLES `Program` WRITE;
/*!40000 ALTER TABLE `Program` DISABLE KEYS */;
INSERT INTO `Program` VALUES (1,'Computer Science','CS','2013-01-01',1),(2,'Finance and Accounting','FA','2011-01-01',1);
/*!40000 ALTER TABLE `Program` ENABLE KEYS */;
UNLOCK TABLES;
--
-- Dumping data for table `Record`
--

LOCK TABLES `Record` WRITE;
/*!40000 ALTER TABLE `Record` DISABLE KEYS */;
INSERT INTO `Record` VALUES (1,'NG',1,1);
/*!40000 ALTER TABLE `Record` ENABLE KEYS */;
UNLOCK TABLES;
--
-- Dumping data for table `Record_records_sessions_Session`
--

LOCK TABLES `Record_records_sessions_Session` WRITE;
/*!40000 ALTER TABLE `Record_records_sessions_Session` DISABLE KEYS */;
INSERT INTO `Record_records_sessions_Session` VALUES (1,1),(1,2);
/*!40000 ALTER TABLE `Record_records_sessions_Session` ENABLE KEYS */;
UNLOCK TABLES;
--
-- Dumping data for table `Registration_Exam`
--

LOCK TABLES `Registration_Exam` WRITE;
/*!40000 ALTER TABLE `Registration_Exam` DISABLE KEYS */;
INSERT INTO `Registration_Exam` VALUES (1,'R','2019-12-23 13:00:00',1,1);
/*!40000 ALTER TABLE `Registration_Exam` ENABLE KEYS */;
UNLOCK TABLES;
--
-- Dumping data for table `Role`
--

LOCK TABLES `Role` WRITE;
/*!40000 ALTER TABLE `Role` DISABLE KEYS */;
INSERT INTO `Role` VALUES (2,'assistant'),(3,'lecturer'),(1,'student');
/*!40000 ALTER TABLE `Role` ENABLE KEYS */;
UNLOCK TABLES;
--
-- Dumping data for table `Room`
--

LOCK TABLES `Room` WRITE;
/*!40000 ALTER TABLE `Room` DISABLE KEYS */;
INSERT INTO `Room` VALUES (1,NULL,50,'A301'),(2,NULL,100,'B101');
/*!40000 ALTER TABLE `Room` ENABLE KEYS */;
UNLOCK TABLES;
--
-- Dumping data for table `Session`
--

LOCK TABLES `Session` WRITE;
/*!40000 ALTER TABLE `Session` DISABLE KEYS */;
INSERT INTO `Session` VALUES (1,'09:15:00','2019-12-03','12:00:00',1,1),(2,'09:15:00','2019-12-04','12:00:00',1,1);
/*!40000 ALTER TABLE `Session` ENABLE KEYS */;
UNLOCK TABLES;
--
-- Dumping data for table `Student`
--

LOCK TABLES `Student` WRITE;
/*!40000 ALTER TABLE `Student` DISABLE KEYS */;
INSERT INTO `Student` VALUES (1,'1997-07-15','Nguyen',NULL,'M','Vietnam','1141818','Duy',NULL),(2,'1995-03-04','An','Ngoc Thoai','F','Vietnam','1150302','Bui',NULL),(3,'1996-06-14','Hoang','Phuoc Bao','M','Vietnam','9999999','Nguyen',3);
/*!40000 ALTER TABLE `Student` ENABLE KEYS */;
UNLOCK TABLES;
--
-- Dumping data for table `University`
--

LOCK TABLES `University` WRITE;
/*!40000 ALTER TABLE `University` DISABLE KEYS */;
INSERT INTO `University` VALUES (1,'1978-01-01','University of imagination'),(2,'1990-12-31','University of Interest');
/*!40000 ALTER TABLE `University` ENABLE KEYS */;
UNLOCK TABLES;
-- Dump completed on 2020-01-16 14:16:33

SET FOREIGN_KEY_CHECKS=1;
