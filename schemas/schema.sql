CREATE DATABASE bank;

USE bank;

CREATE TABLE users (
  username VARCHAR(15) NOT NULL,
  password VARCHAR(15),
  PRIMARY KEY (username)
);

CREATE TABLE account (
  username VARCHAR(15) NOT NULL REFERENCES users (username),
  balance  DOUBLE,
  PRIMARY KEY (username)
);

CREATE TABLE login (
  sessionid      TEXT        NOT NULL,
  username       VARCHAR(15) NOT NULL REFERENCES users (username),
  expirationtime TIMESTAMP   NOT NULL,
  PRIMARY KEY (sessionid)
);

CREATE TABLE account_history (
  id        INT NOT NULL AUTO_INCREMENT,
  username  VARCHAR(15) REFERENCES users (username),
  date      TIMESTAMP,
  operation VARCHAR(10),
  amount    DOUBLE,
  PRIMARY KEY (id)
);

DELIMITER #

CREATE TRIGGER operation_history BEFORE UPDATE ON account
FOR EACH ROW
  BEGIN
    DECLARE bdate TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
    DECLARE boperation VARCHAR(10) DEFAULT '';
    DECLARE bamount DOUBLE DEFAULT 0;
    IF (NEW.balance < OLD.balance)
    THEN
      SET boperation = 'withdraw';
      SET bamount = old.balance - new.balance;
    ELSE
      SET boperation = 'deposit';
      SET bamount = new.balance - old.balance;
    END IF;
    INSERT INTO account_history (date, username, operation, amount) VALUES (bdate, new.username, boperation, bamount);
  END #
DELIMITER ;


CREATE DATABASE bank_test;

USE bank_test;

CREATE TABLE users (
  username VARCHAR(15) NOT NULL,
  password VARCHAR(15),
  PRIMARY KEY (username)
);

CREATE TABLE account (
  username VARCHAR(15) NOT NULL REFERENCES users (username),
  balance  DOUBLE,
  PRIMARY KEY (username)
);

CREATE TABLE login (
  sessionid      TEXT        NOT NULL,
  username       VARCHAR(15) NOT NULL REFERENCES users (username),
  expirationtime TIMESTAMP   NOT NULL,
  PRIMARY KEY (sessionid)
);

CREATE TABLE account_history (
  id        INT NOT NULL AUTO_INCREMENT,
  username  VARCHAR(15) REFERENCES users (username),
  date      TIMESTAMP,
  operation VARCHAR(10),
  amount    DOUBLE,
  PRIMARY KEY (id)
);

CREATE TRIGGER operation_history BEFORE UPDATE ON account
FOR EACH ROW
  BEGIN
    DECLARE bdate TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
    DECLARE boperation VARCHAR(10) DEFAULT '';
    DECLARE bamount DOUBLE DEFAULT 0;
    IF (NEW.balance < OLD.balance)
    THEN
      SET boperation = 'withdraw';
      SET bamount = old.balance - new.balance;
    ELSE
      SET boperation = 'deposit';
      SET bamount = new.balance - old.balance;
    END IF;
    INSERT INTO account_history (date, username, operation, amount) VALUES (bdate, new.username, boperation, bamount);
  END #
    DELIMITER;