-- create database "rangiffler-userdata" with owner postgres;

create extension if not exists "uuid-ossp";

create table if not exists users
(
    id                      UUID unique        not null default uuid_generate_v1(),
    username                varchar(50) unique not null,
    first_name               varchar(255),
    last_name                 varchar(255),
    avatar                   bytea,
    primary key (id, username)
);

alter table users
    owner to postgres;

create table if not exists users_friends
(
    id                      UUID unique        not null default uuid_generate_v1(),
    user_id                UUID not null,
    friend_id              UUID not null,
    friend_Status                 varchar(50),
    constraint fk_user_users foreign key (user_id) references users (id),
    constraint fk_friend_users foreign key (friend_id) references users (id)
);

alter table users_friends
    owner to postgres;