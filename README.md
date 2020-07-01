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

Clone repository to your local machine and run with Spring Boot Maven Plugin:

```
git clone https://github.com/mikhail-karpov/employee-management-system.git
cd employee-management-system
mvn spring-boot:run
```

Open new tab in your browser on `http://localhost:8080/`

Spring Boot automatically creates schema with DDL and DML scripts in `schema.sql` 
and `data.sql` files. If you want to disable this feature every time on start up, 
comment out or delete this line in `application.properties` file:

```
spring.datasource.initialization-mode=always
```