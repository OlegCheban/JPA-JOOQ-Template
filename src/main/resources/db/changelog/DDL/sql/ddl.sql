--liquibase formatted sql

--changeset ddl

drop sequence if exists person_seq;
create sequence person_seq start with 1 increment by 10;

create table if not exists person(
    id bigint not null,
    name varchar(255) not null,
    age integer,
    primary key (id)
);

drop sequence if exists person_phone_seq;
create sequence person_phone_seq start with 1 increment by 10;

create table if not exists person_phone(
    id bigint not null,
    person_id bigint not null,
    phone_number varchar(255) not null,
    constraint fk_phone_person foreign key (person_id) references person (id),
    constraint uk_phone_person unique (person_id, phone_number)
);