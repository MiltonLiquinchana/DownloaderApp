DROP DATABASE IF EXISTS DOWNLOADER;
CREATE DATABASE IF NOT EXISTS DOWNLOADER;

USE DOWNLOADER;

#Tabla que guarda la categoria de archivo
DROP TABLE IF EXISTS Category;
CREATE TABLE IF NOT EXISTS Category(
PK_CATEGORY INT PRIMARY KEY AUTO_INCREMENT,
C_Name VARCHAR(100) NOT NULL UNIQUE,
C_FileOutputPath VARCHAR(100) NOT NULL
);

DROP TABLE IF EXISTS FileType;
CREATE TABLE IF NOT EXISTS FileType(
PK_FILETYPE INT PRIMARY KEY AUTO_INCREMENT,
FT_Type VARCHAR(10) NOT NULL UNIQUE
);

DROP TABLE IF EXISTS DownloadFile;
CREATE TABLE IF NOT EXISTS DownloadFile(
PK_DOWNLOADFILE INT PRIMARY KEY AUTO_INCREMENT,
DF_URL TEXT NOT NULL,
DF_Name VARCHAR(250) NOT NULL UNIQUE,
DF_FileLength DECIMAL(50,15) NOT NULL,
DF_Description VARCHAR(250),
DF_Status BOOLEAN NOT NULL,
FK_FILETYPE INT NOT NULL,
FOREIGN KEY (FK_FILETYPE) REFERENCES FileType(PK_FILETYPE),
FK_CATEGORY INT NOT NULL,
FOREIGN KEY (FK_CATEGORY) REFERENCES Category(PK_CATEGORY)
);

