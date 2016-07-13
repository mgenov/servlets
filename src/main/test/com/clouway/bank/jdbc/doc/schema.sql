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

create table accounts(
 email VARCHAR (50),
 balance double PRECISION
 );


 create table transactions (
  date bigint,
  email varchar(30),
  operation varchar(30),
  amount DOUBLE PRECISION,
  currentBalance DOUBLE PRECISION
  );