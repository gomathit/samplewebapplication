# This is a MySQL script for creating 3 databases, their tables, and populating 
# them with informal test data. Tested with version 5.5.8.
#
# The CREATE TABLE statements do not fully comply with SQL-92.
#
# MySQL extensions used here include :
# UNSIGNED, AUTO-INCREMENT, 
# DROP TABLE IF EXISTS, and the use of comments.
#
# SQL-92 validation tool:
# http://developer.mimer.se/validator/parser92/index.tml#parser
#
# The type is set to InnoDB to allow the tables to participate
# in transactions.
#
# Run this script in the current directory, in Windows, like so:
# >C:\mysql\bin\mysql.exe --local-infile=1 -u myname -pmypass < CreateALL.SQL
#

# Ensure the mysql client is using UTF-8
SET CHARACTER SET utf8;

DROP DATABASE IF EXISTS predict;
CREATE DATABASE predict CHARACTER SET = utf8;
USE predict;

CREATE TABLE Users (  
  Id INT UNSIGNED NOT NULL AUTO_INCREMENT,
  LoginName VARCHAR(50)  UNIQUE NOT NULL, 
  ScreenName VARCHAR(50), /* Binary needed to ensure case sensitive. */
  Password VARCHAR(50) BINARY NOT NULL, /* Binary needed to ensure case sensitive. Must hash the password. */
  Email VARCHAR(100) UNIQUE NOT NULL,  /* Used only for password reset. */
  FormSourceId VARCHAR(100) BINARY,   /* Defends against CSRF attacks. */
  PasswordNonce VARCHAR(40),   /* Used only when user loses their password. */
  PasswordNonceCreatedOn DATETIME,  
  PRIMARY KEY (Id)
) Engine=InnoDB;

# Populate tables with some base data.
# *** WARNING *** : this data is for development only, and must be DELETED from production environments.
# The password is in hashed form, and has the cleartext value 'testtest'. 
INSERT INTO Users (Id, LoginName, Password, ScreenName, Email) VALUES(1, 'testeD', '51abb9636078defbf888d8457a7c76f85c8f114c', 'Teste D', 'blah4@blah.com');

# In the style demanded by Tomcat (not a normalized form).
# Tomcat has the weird constraint that the column names must match between UserRole.Name and Users.Name.
CREATE TABLE UserRole (  
  LoginName VARCHAR(50) NOT NULL, 
  Role VARCHAR(50) NOT NULL Default 'user',  /*default to the user role for safety */
  INDEX (LoginName),
  FOREIGN KEY (LoginName) REFERENCES Users(LoginName),
  PRIMARY KEY (LoginName, Role)
) Engine=InnoDB;
INSERT INTO UserRole (LoginName, Role) VALUES('testeD', 'user');

CREATE TABLE PredictionList (  
  Id INT UNSIGNED NOT NULL AUTO_INCREMENT,
  Title VARCHAR(50) NOT NULL, 
  CreationDate DATE NOT NULL,
  UserFK INT UNSIGNED NOT NULL,
  INDEX(UserFK),
  FOREIGN KEY (UserFK) REFERENCES Users(Id),
  PRIMARY KEY (Id)
) Engine=InnoDB;
INSERT INTO PredictionList(Id, Title, CreationDate, UserFK) VALUES(1, 'Test List', CURRENT_DATE, 1);

# Code table which does NOT follow the standard form, since it carries numeric weights
CREATE TABLE Outcome (  
  Id TINYINT UNSIGNED NOT NULL AUTO_INCREMENT,
  Text VARCHAR(50) UNIQUE NOT NULL,
  Weight TINYINT UNSIGNED, /* 0..100 */
  DisplayOrder TINYINT UNSIGNED NOT NULL,
  PRIMARY KEY (Id)
) Engine=InnoDB;
INSERT INTO Outcome(Id, Text, Weight, DisplayOrder) VALUES (1, 'Right', 100, 1);
INSERT INTO Outcome(Id, Text, Weight, DisplayOrder) VALUES (2, 'Mostly Right', 75, 2);
INSERT INTO Outcome(Id, Text, Weight, DisplayOrder) VALUES (3, 'Half Right', 50, 3);
INSERT INTO Outcome(Id, Text, Weight, DisplayOrder) VALUES (4, 'Mostly Wrong', 25, 4);
INSERT INTO Outcome(Id, Text, Weight, DisplayOrder) VALUES (5, 'Wrong', 0, 5);
INSERT INTO Outcome(Id, Text, Weight, DisplayOrder) VALUES (6, 'Undecidable', NULL, 6);

CREATE TABLE Prediction (  
  Id INT UNSIGNED NOT NULL AUTO_INCREMENT,
  PredictionListFK INT UNSIGNED NOT NULL,
  Text VARCHAR(255) NOT NULL, 
  CreationDate DATETIME NOT NULL,
  Remark TEXT, /* Large, 64K */
  OutcomeFK TINYINT UNSIGNED,
  OutcomeDate DATE, /*If outcome is changed, so is this field. */
  INDEX(PredictionListFK),
  INDEX(OutcomeFK),
  INDEX(Text), /* Needed for searches. */
  FOREIGN KEY (PredictionListFK) REFERENCES PredictionList(Id),
  FOREIGN KEY (OutcomeFK) REFERENCES Outcome(Id),
  PRIMARY KEY (Id)
) Engine=InnoDB;
INSERT INTO Prediction(Id, PredictionListFK, Text, CreationDate, Remark) VALUES(1, 1, 'Test Prediction', CURRENT_DATE, 'Test Remark');
