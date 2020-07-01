CREATE TABLE IF NOT EXISTS employees (
  id bigint(11) NOT NULL AUTO_INCREMENT,
  email varchar(50) DEFAULT NULL,
  first_name varchar(20) DEFAULT NULL,
  last_name varchar(20) DEFAULT NULL,
  role varchar(20) NOT NULL,
  PRIMARY KEY (id)
);
