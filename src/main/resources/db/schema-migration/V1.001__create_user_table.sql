create table user_table
(
    id       varchar(255) primary key,
    username varchar(120) not null unique,
    password varchar(255) not null,
    salt bytea not null
);
