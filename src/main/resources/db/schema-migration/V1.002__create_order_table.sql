create table order_table
(
    id       varchar(255) primary key,
    user_id  varchar(255) not null references user_table(id),
    order_date timestamp not null
);
