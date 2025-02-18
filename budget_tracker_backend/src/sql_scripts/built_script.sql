USE budgettracker;

DROP TABLE if EXISTS `transaction`;
drop table if exists account;
drop table if exists `user`;

create table user
(
    id           bigint auto_increment primary key,
    username     varchar(20)  not null unique,
    password     varchar(255) not null,
    email        varchar(255) not null,
    email_hashed varchar(255) not null unique,
    created_at   datetime     not null default current_timestamp
);

create table account
(
    id          bigint auto_increment primary key,
    title       varchar(100) not null,
    balance     decimal      not null,
    description text,
    user_id     BIGINT       NOT null,
    created_at  datetime     not null default current_timestamp,
    last_update datetime     not null default current_timestamp on update current_timestamp,
    foreign key (user_id) references user (id)
);

CREATE TABLE `transaction`
(
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    `description` TEXT,
    amount        decimal      NOT NULL,
    `date`        DATETIME NOT NULL           DEFAULT current_timestamp,
    account_id    bigint   not null,
    type          enum ('INCOME', 'EXPENSES') NOT NULL default 'INCOME',
    foreign key (account_id) references account (id)
);