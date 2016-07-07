CREATE DATABASE test;

CREATE TABLE users(
name VARCHAR(30),
email VARCHAR (30),
password VARCHAR (16)
);

create table sessions(
id varcahr(20),
email varchar(30),
timeForLife bigint
);