BEGIN TRANSACTION;
CREATE TABLE IF NOT EXISTS "example" (
	"Id"	INTEGER,
	"Name"	TEXT,
	PRIMARY KEY("Id")
);
CREATE TABLE IF NOT EXISTS "Branch" (
	"branchId"	INTEGER,
	"morningStartTime"	TEXT,
	"morningEndTime"	TEXT,
	"nightStartTime"	TEXT,
	"nightEndTime"	TEXT,
	PRIMARY KEY("branchId")
);
CREATE TABLE IF NOT EXISTS "Employee" (
	"employeeId"	INTEGER,
	"name"	TEXT,
	"personalInfo"	TEXT,
	"baseSalary"	NUMERIC,
	"password"	TEXT,
	PRIMARY KEY("employeeId")
);
CREATE TABLE IF NOT EXISTS "ShiftId" (
	"shiftId"	INTEGER,
	PRIMARY KEY("shiftId")
);
CREATE TABLE IF NOT EXISTS "BankInformation" (
	"employeeId"	INTEGER,
	"bankName"	TEXT,
	"bankAccountNumber"	TEXT,
	PRIMARY KEY("employeeId"),
	FOREIGN KEY("employeeId") REFERENCES "Employee"("employeeId") ON DELETE CASCADE
);
CREATE TABLE IF NOT EXISTS "Driver" (
	"employeeId"	INTEGER,
	"driverLicense"	TEXT,
	PRIMARY KEY("employeeId"),
	FOREIGN KEY("employeeId") REFERENCES "Employee"("employeeId") ON DELETE CASCADE
);
CREATE TABLE IF NOT EXISTS "EmployeeRoles" (
	"employeeId"	INTEGER,
	"role"	TEXT,
	FOREIGN KEY("employeeId") REFERENCES "Employee"("employeeId") ON DELETE CASCADE,
	PRIMARY KEY("employeeId","role")
);
CREATE TABLE IF NOT EXISTS "EmploymentContract" (
	"employeeId"	INTEGER,
	"startDate"	TEXT,
	"termsAndConditions"	TEXT,
	FOREIGN KEY("employeeId") REFERENCES "Employee"("employeeId") ON DELETE CASCADE,
	PRIMARY KEY("employeeId")
);
CREATE TABLE IF NOT EXISTS "Salary" (
	"employeeId"	INTEGER,
	"date"	TEXT,
	"salary"	NUMERIC,
	"bonus"	NUMERIC,
	FOREIGN KEY("employeeId") REFERENCES "Employee"("employeeId") ON DELETE CASCADE,
	PRIMARY KEY("employeeId","date")
);
CREATE TABLE IF NOT EXISTS "Shift" (
	"shiftDate"	TEXT,
	"shiftType"	TEXT,
	"shiftID"	INTEGER,
	"branchID"	INTEGER,
	FOREIGN KEY("branchID") REFERENCES "Branch"("branchId") ON DELETE CASCADE,
	PRIMARY KEY("shiftID")
);
CREATE TABLE IF NOT EXISTS "ShiftApproved" (
	"shiftID"	INTEGER,
	"employeeID"	INTEGER,
	"role"	TEXT,
	FOREIGN KEY("employeeID") REFERENCES "Employee"("employeeId") ON DELETE CASCADE,
	FOREIGN KEY("shiftID") REFERENCES "Shift"("shiftID") ON DELETE CASCADE
);
CREATE TABLE IF NOT EXISTS "ShiftBlocked" (
	"employeeID"	INTEGER,
	"shiftID"	INTEGER,
	FOREIGN KEY("employeeID") REFERENCES "Employee"("employeeId") ON DELETE CASCADE,
	FOREIGN KEY("shiftID") REFERENCES "Shift"("shiftID") ON DELETE CASCADE,
	PRIMARY KEY("employeeID","shiftID")
);
CREATE TABLE IF NOT EXISTS "ShiftRequested" (
	"shiftID"	INTEGER,
	"employeeID"	INTEGER,
	FOREIGN KEY("employeeID") REFERENCES "Employee"("employeeId") ON DELETE CASCADE,
	FOREIGN KEY("shiftID") REFERENCES "Shift"("shiftID") ON DELETE CASCADE,
	PRIMARY KEY("shiftID","employeeID")
);
CREATE TABLE IF NOT EXISTS "ShiftRequired" (
	"shiftID"	INTEGER,
	"Role"	TEXT,
	"amount"	INTEGER,
	FOREIGN KEY("shiftID") REFERENCES "Shift"("shiftID") ON DELETE CASCADE,
	PRIMARY KEY("Role","shiftID")
);
INSERT INTO "example" VALUES (1,'John');
INSERT INTO "example" VALUES (2,'Branch2');
INSERT INTO "example" VALUES (3,'C');
INSERT INTO "example" VALUES (4,'D');
INSERT INTO "example" VALUES (5,'55');
INSERT INTO "example" VALUES (6,'69');
INSERT INTO "example" VALUES (7,'77');
INSERT INTO "Branch" VALUES (0,'08:00','21:00','21:00','22:00');
INSERT INTO "Branch" VALUES (1,'08:00','16:00','16:00','21:00');
INSERT INTO "Branch" VALUES (2,'08:00','16:00','16:00','21:30');
INSERT INTO "Branch" VALUES (3,'09:00','15:00','16:00','21:30');
INSERT INTO "Branch" VALUES (4,'10:00','14:00','15:00','21:30');
INSERT INTO "Employee" VALUES (101,'Jill','Bla Bla',1000,'123');
INSERT INTO "Employee" VALUES (123,'John','Bla Bla',2000,'123');
INSERT INTO "Employee" VALUES (456,'Jane','Bla Bla',1500,'123');
INSERT INTO "Employee" VALUES (789,'Jack','Bla Bla',1500,'123');
INSERT INTO "Employee" VALUES (1818,'Miron','Bla Bla',1000,'123');
INSERT INTO "Employee" VALUES (8181,'Ronmi','Bla Bla',1000,'123');
INSERT INTO "Employee" VALUES (8200,'Itay','Bla Bla',1000,'123');
INSERT INTO "ShiftId" VALUES (9);
INSERT INTO "BankInformation" VALUES (101,'12','404');
INSERT INTO "BankInformation" VALUES (123,'12','101');
INSERT INTO "BankInformation" VALUES (456,'12','202');
INSERT INTO "BankInformation" VALUES (789,'12','303');
INSERT INTO "BankInformation" VALUES (1818,'12','707');
INSERT INTO "BankInformation" VALUES (8181,'12','505');
INSERT INTO "BankInformation" VALUES (8200,'12','606');
INSERT INTO "Driver" VALUES (1818,'COLD_HEAVY');
INSERT INTO "Driver" VALUES (8181,'COLD_HEAVY');
INSERT INTO "Driver" VALUES (8200,'COLD_LIGHT');
INSERT INTO "EmployeeRoles" VALUES (123,'MANAGER');
INSERT INTO "EmployeeRoles" VALUES (456,'CASHIER');
INSERT INTO "EmployeeRoles" VALUES (456,'STOREKEEPER');
INSERT INTO "EmployeeRoles" VALUES (789,'CASHIER');
INSERT INTO "EmployeeRoles" VALUES (789,'STEWARD');
INSERT INTO "EmployeeRoles" VALUES (101,'CASHIER');
INSERT INTO "EmployeeRoles" VALUES (8181,'DRIVER');
INSERT INTO "EmployeeRoles" VALUES (8200,'DRIVER');
INSERT INTO "EmployeeRoles" VALUES (1818,'DRIVER');
INSERT INTO "EmploymentContract" VALUES (101,'2023-01-01 00:00:00','Scam conditions');
INSERT INTO "EmploymentContract" VALUES (123,'2023-01-01 00:00:00','Scam conditions+management');
INSERT INTO "EmploymentContract" VALUES (456,'2023-01-01 00:00:00','Scam conditions+');
INSERT INTO "EmploymentContract" VALUES (789,'2023-01-01 00:00:00','Scam conditions+');
INSERT INTO "EmploymentContract" VALUES (1818,'2023-01-01 00:00:00','Drive Fast');
INSERT INTO "EmploymentContract" VALUES (8181,'2023-01-01 00:00:00','Drive Fast');
INSERT INTO "EmploymentContract" VALUES (8200,'2023-01-01 00:00:00','Drive Fast');
INSERT INTO "Shift" VALUES ('01/01/2024','MORNING',0,1);
INSERT INTO "Shift" VALUES ('02/01/2024','MORNING',1,1);
INSERT INTO "Shift" VALUES ('01/01/2024','MORNING',2,2);
INSERT INTO "Shift" VALUES ('02/01/2024','MORNING',3,2);
INSERT INTO "Shift" VALUES ('21/05/2023','MORNING',4,0);
INSERT INTO "Shift" VALUES ('01/01/2024','NIGHT',5,1);
INSERT INTO "Shift" VALUES ('02/01/2024','NIGHT',6,1);
INSERT INTO "Shift" VALUES ('01/01/2024','NIGHT',7,2);
INSERT INTO "Shift" VALUES ('02/01/2024','NIGHT',8,2);
INSERT INTO "ShiftApproved" VALUES (4,8181,'DRIVER');
INSERT INTO "ShiftBlocked" VALUES (8181,4);
INSERT INTO "ShiftRequested" VALUES (4,8200);
INSERT INTO "ShiftRequested" VALUES (4,1818);
INSERT INTO "ShiftRequired" VALUES (0,'MANAGER',1);
INSERT INTO "ShiftRequired" VALUES (1,'MANAGER',1);
INSERT INTO "ShiftRequired" VALUES (2,'MANAGER',1);
INSERT INTO "ShiftRequired" VALUES (3,'MANAGER',1);
INSERT INTO "ShiftRequired" VALUES (4,'MANAGER',1);
INSERT INTO "ShiftRequired" VALUES (4,'DRIVER',1);
INSERT INTO "ShiftRequired" VALUES (5,'MANAGER',1);
INSERT INTO "ShiftRequired" VALUES (6,'MANAGER',1);
INSERT INTO "ShiftRequired" VALUES (7,'MANAGER',1);
INSERT INTO "ShiftRequired" VALUES (8,'MANAGER',1);
COMMIT;
