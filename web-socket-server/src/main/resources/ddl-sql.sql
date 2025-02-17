create database message_db;
use message_db;

drop table messages;
drop table user;

create table user
(
    id       INT AUTO_INCREMENT PRIMARY KEY,
    username varchar(255) not null unique,
    created  DATETIME     not null
);

create table messages
(
    id           INT auto_increment primary key,
    message_from varchar(255) not null,
    message_to   varchar(255) null,
    text         varchar(255) not null,
    seen         boolean      not null,
    created      datetime     not null,
    type         varchar(255) not null,
    FOREIGN KEY (message_from) REFERENCES user(username) ON DELETE CASCADE,
    FOREIGN KEY (message_to) REFERENCES user(username) ON DELETE CASCADE

);