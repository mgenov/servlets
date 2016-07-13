CREATE DATABASE test;

CREATE TABLE users(
name VARCHAR(30),
email VARCHAR (30),
password VARCHAR (16)
);

 create table sessions(
 id VARCHAR (50),
 email varchar(30),
 expirationTime bigint
 );

 create table transactions (
  date bigint,
  email varchar(30),
  operation varchar(30),
  currentBalance double precision)