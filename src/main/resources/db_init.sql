CREATE DATABASE IF NOT EXISTS `employeedb`;
CREATE USER IF NOT EXISTS 'java'@'localhost' IDENTIFIED BY 'javapassword';
GRANT ALL PRIVILEGES ON employeedb.* TO 'java'@'localhost';