create table users
(
    id uuid primary key ,
    name varchar(256) not null ,
    surname varchar(256)not null ,
    email varchar(256) not null unique,
    password varchar(256) not null,
    role varchar(16) not null default 'USER',
    provider varchar(16) not null default 'LOCAL',
    is_verified bool default false not null
);

create table books
(
    id       UUID primary key,
    name     varchar(256)  not null,
    author   varchar(256)  not null,
    is_taken bool default false not null,
    user_id uuid

);

