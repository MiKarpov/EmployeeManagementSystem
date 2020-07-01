# Employee Management System

Simple CRUD web app.

## Technologies

* Spring Boot 2.2
* Spring Data JPA
* MySQL 8.0
* Thymeleaf 3.0
* Bootstrap 4.4

## Setup

Create MYSQL database and user:
```
CREATE DATABASE IF NOT EXISTS `employeedb`;
CREATE USER IF NOT EXISTS 'java'@'localhost' IDENTIFIED BY 'javapassword';
GRANT ALL PRIVILEGES ON employeedb.* TO 'java'@'localhost';
```

Clone repository and run with spring boot maven plugin:
```
git clone https://github.com/mikhail-karpov/employee-management-system.git
cd employee-management-system
mvn spring-boot:run
```
Note that the database table will be created and populated with the source data every time you launch the app. 
If you want to disable this feature, update the application.properties file:
```
spring.jpa.generate-ddl=false
```
