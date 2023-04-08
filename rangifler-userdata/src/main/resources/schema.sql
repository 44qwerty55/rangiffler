-- create database "rangiffler-userdata" with owner postgres;

create extension if not exists "uuid-ossp";

create table if not exists users
(
    id                      UUID unique        not null default uuid_generate_v1(),
    username                varchar(50) unique not null,
    first_name               varchar(255),
    last_name                 varchar(255),
    avatar                   bytea,
    friend_Status                varchar(50) ,
    primary key (id, username)
);

alter table users
    owner to postgres;
