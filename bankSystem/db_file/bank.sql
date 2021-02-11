-- MySQL dump 10.13  Distrib 8.0.22, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: bank
-- ------------------------------------------------------
-- Server version	8.0.22

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `bankaccount`
--

DROP TABLE IF EXISTS `bankaccount`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bankaccount` (
  `BankAccountID` int NOT NULL AUTO_INCREMENT,
  `BACreationDate` datetime DEFAULT NULL,
  `BACurrentBalance` float DEFAULT NULL,
  `CustomerID` int NOT NULL,
  PRIMARY KEY (`BankAccountID`),
  KEY `fk_BankAccount_Customer_idx` (`CustomerID`),
  CONSTRAINT `fk_BankAccount_Customer` FOREIGN KEY (`CustomerID`) REFERENCES `customer` (`CustomerID`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bankaccount`
--

LOCK TABLES `bankaccount` WRITE;
/*!40000 ALTER TABLE `bankaccount` DISABLE KEYS */;
INSERT INTO `bankaccount` VALUES (9,'2020-12-26 00:00:00',1000,2),(10,'2020-12-27 07:40:31',1000,7),(11,'2020-12-26 00:00:00',500,1),(15,'2020-12-25 00:00:00',1000,3),(19,'2020-12-27 06:02:27',1000,6),(22,'2020-12-27 00:00:00',1100,4),(23,'2020-12-27 05:01:34',700,5);
/*!40000 ALTER TABLE `bankaccount` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `banktransaction`
--

DROP TABLE IF EXISTS `banktransaction`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `banktransaction` (
  `BankTransactionID` int NOT NULL AUTO_INCREMENT,
  `BTCreationDate` datetime DEFAULT NULL,
  `BTAmount` float DEFAULT NULL,
  `BTToAccount` int NOT NULL,
  `BTFromAccount` int NOT NULL,
  PRIMARY KEY (`BankTransactionID`),
  KEY `fk_BankTransaction_BankAccount1_idx` (`BTToAccount`),
  KEY `fk_BankTransaction_BankAccount2_idx` (`BTFromAccount`),
  CONSTRAINT `fk_BankTransaction_BankAccount1` FOREIGN KEY (`BTToAccount`) REFERENCES `bankaccount` (`BankAccountID`),
  CONSTRAINT `fk_BankTransaction_BankAccount2` FOREIGN KEY (`BTFromAccount`) REFERENCES `bankaccount` (`BankAccountID`)
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `banktransaction`
--

LOCK TABLES `banktransaction` WRITE;
/*!40000 ALTER TABLE `banktransaction` DISABLE KEYS */;
INSERT INTO `banktransaction` VALUES (33,'2020-12-27 07:29:49',100,15,9),(36,'2020-12-27 07:33:46',200,11,23);
/*!40000 ALTER TABLE `banktransaction` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customer`
--

DROP TABLE IF EXISTS `customer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customer` (
  `CustomerID` int NOT NULL AUTO_INCREMENT,
  `CustomerName` varchar(45) DEFAULT NULL,
  `CustomerAddress` varchar(45) DEFAULT NULL,
  `CustomerMobile` int DEFAULT NULL,
  `CustomerPassword` varchar(45) NOT NULL,
  PRIMARY KEY (`CustomerID`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customer`
--

LOCK TABLES `customer` WRITE;
/*!40000 ALTER TABLE `customer` DISABLE KEYS */;
INSERT INTO `customer` VALUES (1,'Mostafa','hjhj',10,'0'),(2,'Hatem','maadi',100,'0'),(3,'Stannly','giza',100,'0'),(4,'Mohamed','egypt',100,'0'),(5,'Mariam','zahraa',550,'0'),(6,'Ahmed','dokki',60,'0'),(7,'Menna','darslam',50,'0'),(8,'Yasser','helwan',10,'0'),(9,'Mona','nasr',25,'0'),(10,'Ebrahim','cairo',65,'0');
/*!40000 ALTER TABLE `customer` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-12-27 19:57:23
