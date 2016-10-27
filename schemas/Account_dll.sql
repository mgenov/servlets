create table accounts(
ID        INT            NOT NULL PRIMARY KEY AUTO_INCREMENT,
Name      VARCHAR(50)    NOT NULL,
Password  VARCHAR(18)    NOT NULL,
Amount    DOUBLE         NOT NULL
);