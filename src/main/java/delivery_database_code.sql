BEGIN TRANSACTION;
CREATE TABLE IF NOT EXISTS "Providers" (
	"id"	INTEGER,
	"address"	TEXT,
	"shipmentArea"	TEXT,
	"phone"	TEXT,
	"contactName"	TEXT,
	"northCoordinate"	INTEGER,
	"eastCoordinate"	INTEGER,
	PRIMARY KEY("id")
);
CREATE TABLE IF NOT EXISTS "Stores" (
	"id"	INTEGER,
	"address"	TEXT,
	"shipmentArea"	TEXT,
	"phone"	TEXT,
	"contactName"	TEXT,
	"northCoordinate"	INTEGER,
	"eastCoordinate"	INTEGER,
	PRIMARY KEY("id")
);
CREATE TABLE IF NOT EXISTS "ShipmentFactories" (
	"documentIdGenerator"	INTEGER
);
CREATE TABLE IF NOT EXISTS "Trucks" (
	"licenseNumber"	TEXT,
	"model"	TEXT,
	"weight"	INTEGER,
	"maxWeight"	INTEGER,
	"available"	INTEGER,
	"licenseType"	TEXT,
	PRIMARY KEY("licenseNumber")
);
CREATE TABLE IF NOT EXISTS "SupplyLeftovers" (
	"providerId"	INTEGER,
	"itemId"	INTEGER,
	"amount"	INTEGER,
	FOREIGN KEY("providerId") REFERENCES "Providers"("id") ON DELETE CASCADE,
	FOREIGN KEY("itemId") REFERENCES "Items"("id") ON DELETE CASCADE
);
CREATE TABLE IF NOT EXISTS "StockLeftovers" (
	"storeId"	INTEGER,
	"itemId"	INTEGER,
	"amount"	INTEGER,
	FOREIGN KEY("itemId") REFERENCES "Items"("id") ON DELETE CASCADE,
	FOREIGN KEY("storeId") REFERENCES "Stores"("id") ON DELETE CASCADE
);
CREATE TABLE IF NOT EXISTS "Shipments" (
	"id"	INTEGER,
	"date"	TEXT,
	"timeOfExit"	TEXT,
	"truckLicenseNumber"	TEXT,
	"driverName"	TEXT,
	"sourceId"	INTEGER,
	"weight"	INTEGER,
	"isActive"	INTEGER,
	"overWeight"	INTEGER,
	"treatment"	INTEGER,
	PRIMARY KEY("id"),
	FOREIGN KEY("sourceId") REFERENCES "Providers"("id") ON DELETE CASCADE,
	FOREIGN KEY("truckLicenseNumber") REFERENCES "Trucks"("licenseNumber") ON DELETE CASCADE
);
CREATE TABLE IF NOT EXISTS "ShipmentStores" (
	"shipmentId"	INTEGER,
	"storeId"	INTEGER,
	FOREIGN KEY("shipmentId") REFERENCES "Shipments"("id") ON DELETE CASCADE,
	FOREIGN KEY("storeId") REFERENCES "Stores"("id") ON DELETE CASCADE
);
CREATE TABLE IF NOT EXISTS "ShipmentStoreItems" (
	"shipmentId"	INTEGER,
	"storeId"	INTEGER,
	"itemId"	INTEGER,
	"amount"	INTEGER,
	FOREIGN KEY("shipmentId") REFERENCES "Shipments"("id") ON DELETE CASCADE,
	FOREIGN KEY("storeId") REFERENCES "Stores"("id") ON DELETE CASCADE,
	FOREIGN KEY("itemId") REFERENCES "Items"("id") ON DELETE CASCADE
);
CREATE TABLE IF NOT EXISTS "ShipmentProviders" (
	"shipmentId"	INTEGER,
	"providerId"	INTEGER,
	"visited"	INTEGER,
	FOREIGN KEY("providerId") REFERENCES "Providers"("id") ON DELETE CASCADE,
	FOREIGN KEY("shipmentId") REFERENCES "Shipments"("id") ON DELETE CASCADE
);
CREATE TABLE IF NOT EXISTS "ShipmentProviderItems" (
	"shipmentId"	INTEGER,
	"providerId"	INTEGER,
	"itemId"	INTEGER,
	"amount"	INTEGER,
	FOREIGN KEY("shipmentId") REFERENCES "Shipments"("id") ON DELETE CASCADE,
	FOREIGN KEY("providerId") REFERENCES "Providers"("id") ON DELETE CASCADE,
	FOREIGN KEY("itemId") REFERENCES "Items"("id") ON DELETE CASCADE
);
CREATE TABLE IF NOT EXISTS "ShipmentEstimations" (
	"shipmentId"	INTEGER,
	"storeId"	INTEGER,
	"estimation"	TEXT,
	FOREIGN KEY("shipmentId") REFERENCES "Shipments"("id") ON DELETE CASCADE,
	FOREIGN KEY("storeId") REFERENCES "Stores"("id") ON DELETE CASCADE
);
CREATE TABLE IF NOT EXISTS "FileItems" (
	"documentId"	INTEGER,
	"itemId"	INTEGER,
	"amount"	INTEGER,
	FOREIGN KEY("itemId") REFERENCES "Items"("id") ON DELETE CASCADE,
	FOREIGN KEY("documentId") REFERENCES "DocumentedFiles"("documentId") ON DELETE CASCADE
);
CREATE TABLE IF NOT EXISTS "DocumentedFiles" (
	"documentId"	INTEGER,
	"shipmentId"	INTEGER,
	"storeId"	INTEGER,
	FOREIGN KEY("storeId") REFERENCES "Stores"("id") ON DELETE CASCADE,
	FOREIGN KEY("shipmentId") REFERENCES "Shipments"("id") ON DELETE CASCADE,
	PRIMARY KEY("documentId")
);
CREATE TABLE IF NOT EXISTS "Items" (
	"id"	INTEGER,
	"name"	TEXT,
	PRIMARY KEY("id")
);
INSERT INTO "Providers" VALUES (0,'Logistic Center','CENTER','03-1234567','Logistic Center',0,0);
INSERT INTO "Providers" VALUES (5,'Nituz 33','SOUTH','052-6666666','Heil Hamaim',-15,9);
INSERT INTO "Providers" VALUES (6,'Ze-Ahla 5','SOUTH','052-7777777','Golani',-13,14);
INSERT INTO "Providers" VALUES (7,'Kurs 45','SOUTH','052-8888888','Guy Shani',-19,-7);
INSERT INTO "Stores" VALUES (0,'Logistic Center','CENTER','03-1234567','Logistic Center',0,0);
INSERT INTO "Stores" VALUES (1,'Rager 9','SOUTH','052-1111111','Miri',-10,3);
INSERT INTO "Stores" VALUES (2,'Rager 19','SOUTH','052-2222222','Danel',-10,13);
INSERT INTO "Stores" VALUES (3,'Rager 29','SOUTH','052-3333333','Yael',-10,23);
INSERT INTO "Stores" VALUES (4,'The Village Afula','NORTH','052-4444444','Ron The Farmer(not mi)',-10,33);
INSERT INTO "ShipmentFactories" VALUES (2);
INSERT INTO "Trucks" VALUES ('111-11-111','Mercedes truck',1000,1200,1,'REGULAR_LIGHT');
INSERT INTO "Trucks" VALUES ('222-22-222','Ford truck',1200,1500,1,'REGULAR_HEAVY');
INSERT INTO "Trucks" VALUES ('333-33-333','Toyota truck',2000,2400,1,'COLD_LIGHT');
INSERT INTO "Trucks" VALUES ('444-44-444','Dodge truck',2500,3000,1,'COLD_HEAVY');
INSERT INTO "Trucks" VALUES ('555-55-555','Ferrari truck',2700,3200,1,'COLD_HEAVY');
INSERT INTO "Trucks" VALUES ('666-66-666','Miri truck',3000,3500,0,'COLD_HEAVY');
INSERT INTO "Shipments" VALUES (0,'2023-05-21','08:00','666-66-666','Ronmi',7,-1,1,0,'NONE');
INSERT INTO "ShipmentStores" VALUES (0,1);
INSERT INTO "ShipmentStores" VALUES (0,2);
INSERT INTO "ShipmentStoreItems" VALUES (0,1,0,3);
INSERT INTO "ShipmentStoreItems" VALUES (0,1,1,5);
INSERT INTO "ShipmentStoreItems" VALUES (0,1,3,20);
INSERT INTO "ShipmentStoreItems" VALUES (0,2,0,7);
INSERT INTO "ShipmentStoreItems" VALUES (0,2,1,4);
INSERT INTO "ShipmentStoreItems" VALUES (0,2,7,10);
INSERT INTO "ShipmentProviders" VALUES (0,7,0);
INSERT INTO "ShipmentProviders" VALUES (0,6,0);
INSERT INTO "ShipmentProviders" VALUES (0,5,0);
INSERT INTO "ShipmentProviderItems" VALUES (0,7,1,1);
INSERT INTO "ShipmentProviderItems" VALUES (0,7,7,1);
INSERT INTO "ShipmentProviderItems" VALUES (0,6,0,10);
INSERT INTO "ShipmentProviderItems" VALUES (0,6,1,1);
INSERT INTO "ShipmentProviderItems" VALUES (0,6,7,9);
INSERT INTO "ShipmentProviderItems" VALUES (0,5,1,7);
INSERT INTO "ShipmentProviderItems" VALUES (0,5,3,20);
INSERT INTO "ShipmentEstimations" VALUES (0,1,'12:46:00.443873600');
INSERT INTO "ShipmentEstimations" VALUES (0,2,'12:51:00.443873600');
INSERT INTO "FileItems" VALUES (0,0,3);
INSERT INTO "FileItems" VALUES (0,1,5);
INSERT INTO "FileItems" VALUES (0,3,20);
INSERT INTO "FileItems" VALUES (1,0,7);
INSERT INTO "FileItems" VALUES (1,1,4);
INSERT INTO "FileItems" VALUES (1,7,10);
INSERT INTO "DocumentedFiles" VALUES (0,0,1);
INSERT INTO "DocumentedFiles" VALUES (1,0,2);
INSERT INTO "Items" VALUES (0,'chocolate');
INSERT INTO "Items" VALUES (1,'milk');
INSERT INTO "Items" VALUES (2,'bread');
INSERT INTO "Items" VALUES (3,'eggs');
INSERT INTO "Items" VALUES (4,'cheese');
INSERT INTO "Items" VALUES (5,'meat');
INSERT INTO "Items" VALUES (6,'fish');
INSERT INTO "Items" VALUES (7,'vegetables');
INSERT INTO "Items" VALUES (8,'Shibutzim');
COMMIT;
