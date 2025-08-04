USE exampledb;

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
    balance     DECIMAL(10,2)      not null,
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
    amount        DECIMAL(10,2)               NOT NULL,
    `date`        DATE                        NOT NULL DEFAULT current_timestamp,
    created_date	DATETIME							NOT NULL DEFAULT CURRENT_TIMESTAMP,
    account_id    bigint                      not null,
    type          int default 0,
    `category`		VARCHAR(100) 						NOT NULL DEFAULT 'others',
    foreign key (account_id) references account (id)
);
