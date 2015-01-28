DROP DATABASE IF EXISTS payroll;

CREATE DATABASE payroll;

USE payroll;

CREATE TABLE employees
(
   socialSecurityNumber int NOT NULL PRIMARY KEY,
   firstName varchar(30) NOT NULL,
   lastName varchar(30) NOT NULL,
   birthday date NOT NULL,
   employeeType varchar(30) NOT NULL,
   departmentName varchar(30) NOT NULL
)                 ;
CREATE TABLE salariedEmployees
(
   socialSecurityNumber int NOT NULL PRIMARY KEY,
   weeklySalary DECIMAL(8, 2) NOT NULL,
   bonus DECIMAL(8, 2) NOT NULL,
   FOREIGN KEY (socialSecurityNumber) references employees(socialSecurityNumber)
)             ;
CREATE TABLE commissionEmployees
(
   socialSecurityNumber int NOT NULL PRIMARY KEY,
   grossSales DECIMAL(8, 2) NOT NULL,
   commissionRate DECIMAL(5, 4) NOT NULL,
   bonus DECIMAL(8, 2) NOT NULL,
   FOREIGN KEY (socialSecurityNumber) references employees(socialSecurityNumber)
)             ;
CREATE TABLE hourlyEmployees
(
   socialSecurityNumber int NOT NULL PRIMARY KEY,
   hours DECIMAL(3, 1) NOT NULL,
   wage DECIMAL(8, 2) NOT NULL,
   bonus DECIMAL(8, 2) NOT NULL,
   FOREIGN KEY (socialSecurityNumber) references employees(socialSecurityNumber)
)             ;
CREATE TABLE basePluscommissionEmployees
(
   socialSecurityNumber int NOT NULL PRIMARY KEY,
   grossSales DECIMAL(8, 2) NOT NULL,
   commissionRate DECIMAL(5, 4) NOT NULL,
   baseSalary DECIMAL(8, 2) NOT NULL,
   bonus DECIMAL(8, 2) NOT NULL,
   FOREIGN KEY (socialSecurityNumber) references employees(socialSecurityNumber)
)             ;

insert into employees(socialSecurityNumber, firstName, lastName, birthday, employeeType, departmentName)
values (11111, 'Graham', 'Thomas', '1983-09-15', 'Salaried Employee', 'IT');
insert into employees(socialSecurityNumber, firstName, lastName, birthday, employeeType, departmentName)
values (22222, 'Linton', 'Johnson', '1977-12-25', 'Commission Employee', 'HR');
insert into employees(socialSecurityNumber, firstName, lastName, birthday, employeeType, departmentName)
values (33333, 'Old', 'Gregg', '1945-01-01', 'Hourly Employee', 'Facilities');
insert into employees(socialSecurityNumber, firstName, lastName, birthday, employeeType, departmentName)
values (44444, 'Mother', 'Licker', '1999-12-31', 'Base Plus Commission Employee', 'Finance');

/*
INSERT INTO Authors (FirstName,LastName) VALUES ('Harvey','Deitel')           ;
INSERT INTO Authors (FirstName,LastName) VALUES ('Paul','Deitel')     ;
INSERT INTO Authors (FirstName,LastName) VALUES ('Andrew','Goldberg')    ;
INSERT INTO Authors (FirstName,LastName) VALUES ('David','Choffnes')     ;


INSERT INTO Titles (ISBN,Title,EditionNumber,Copyright) VALUES ('0131869000','Visual Basic 2005 How to Program',3,'2006')          ;
INSERT INTO AuthorISBN (AuthorID,ISBN) VALUES (1,'0131869000')     ;
INSERT INTO AuthorISBN (AuthorID,ISBN) VALUES (2,'0131869000') ;
INSERT INTO Titles (ISBN,Title,EditionNumber,Copyright) VALUES ('0131525239','Visual C# 2005 How to Program',2,'2006')  ;
INSERT INTO AuthorISBN (AuthorID,ISBN) VALUES (1,'0131525239')      ;
INSERT INTO AuthorISBN (AuthorID,ISBN) VALUES (2,'0131525239')      ;

INSERT INTO Titles (ISBN,Title,EditionNumber,Copyright) VALUES ('0132222205','Java How to Program',7,'2007')   ;
INSERT INTO AuthorISBN (AuthorID,ISBN) VALUES (1,'0132222205')                                               ;
INSERT INTO AuthorISBN (AuthorID,ISBN) VALUES (2,'0132222205')                                       ;

INSERT INTO Titles (ISBN,Title,EditionNumber,Copyright) VALUES ('0131857576','C++ How to Program',5,'2005')     ;
INSERT INTO AuthorISBN (AuthorID,ISBN) VALUES (1,'0131857576')                                                  ;
INSERT INTO AuthorISBN (AuthorID,ISBN) VALUES (2,'0131857576')                                                 ;

INSERT INTO Titles (ISBN,Title,EditionNumber,Copyright) VALUES ('0132404168','C How to Program',5,'2007')     ;
INSERT INTO AuthorISBN (AuthorID,ISBN) VALUES (1,'0132404168')                                             ;
INSERT INTO AuthorISBN (AuthorID,ISBN) VALUES (2,'0132404168')                                       ;

INSERT INTO Titles (ISBN,Title,EditionNumber,Copyright) VALUES ('0131450913','Internet & World Wide Web How to Program',3,'2004')    ;
INSERT INTO AuthorISBN (AuthorID,ISBN) VALUES (1,'0131450913')                                                                 ;
INSERT INTO AuthorISBN (AuthorID,ISBN) VALUES (2,'0131450913')                                            ;
INSERT INTO AuthorISBN (AuthorID,ISBN) VALUES (3,'0131450913')                                   ;

INSERT INTO Titles (ISBN,Title,EditionNumber,Copyright) VALUES ('0131828274','Operating Systems',3,'2004')        ;
INSERT INTO AuthorISBN (AuthorID,ISBN) VALUES (1,'0131828274')                                                    ;
INSERT INTO AuthorISBN (AuthorID,ISBN) VALUES (2,'0131828274')                                                    ;
INSERT INTO AuthorISBN (AuthorID,ISBN) VALUES (4,'0131828274')                                                    ;
*/