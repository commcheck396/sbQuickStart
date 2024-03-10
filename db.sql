create database sbQuickStart;

use sbQuickStart;

create table users(
                      id int unsigned auto_increment primary key comment 'ID',
                      username varchar(20) not null unique comment 'Username',
                      password varchar(32) not null comment 'Password',
                      nickname varchar(20) default '' comment 'Nickname',
                      email varchar(100) unique comment 'Email',
                      avatar varchar(128) default '' comment 'Avatar',
                      status int unsigned default 0 comment 'Status',
                      created_time datetime not null comment 'Created Time',
                      updated_time datetime not null comment 'Updated Time'
) comment 'Users';