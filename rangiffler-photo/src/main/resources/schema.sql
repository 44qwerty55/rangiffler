-- create database "rangiffler-photo" with owner postgres;

create extension if not exists "uuid-ossp";


  create table if not exists photos
  (
      id                      UUID unique        not null default uuid_generate_v1(),
      username                varchar(50)  not null,
      photo                  bytea  not null,
      description                varchar(255)  ,
      countries_id UUID         not null,
      name                varchar(255)  not null,
      code                varchar(50)       not null
  );


  alter table photos
      owner to postgres;

