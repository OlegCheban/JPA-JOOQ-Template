--liquibase formatted sql

--changeset ddl

CREATE TABLE customer (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);