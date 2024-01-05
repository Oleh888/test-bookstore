create table book
(
    id               varchar(255) primary key,
    title            varchar(120)   not null,
    author           varchar(100)   not null,
    price            numeric not null,
    publication_year smallint       not null,
    order_id         varchar(255) references order_table (id)
);
