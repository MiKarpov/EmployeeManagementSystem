CREATE SCHEMA IF NOT EXISTS employeedb;

CREATE USER IF NOT EXISTS 'java'@'localhost' IDENTIFIED BY 'javapassword';

GRANT SELECT, INSERT, UPDATE ON employeedb.* TO 'java'@'localhost';