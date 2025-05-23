--liquibase formatted sql

--changeset ddl

drop table if exists person;
create table person (
  id bigint not null,
  name varchar(255),
  primary key (id)
);

drop sequence if exists person_seq;
create sequence person_seq start with 1 increment by 50;