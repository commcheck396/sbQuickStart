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

create table tickets(
                        id int unsigned auto_increment primary key comment 'ID',
                        title varchar(100) not null comment 'Title',
                        description varchar(10000) not null comment 'Description',
                        ownerId int unsigned default 0 comment 'Owner ID',
                        assigneeId int unsigned default 0 comment 'Assignee ID',
                        watcherId varchar(1000) default '' comment 'Watcher ID',
                        cover varchar(128) default '' comment 'Cover',
                        priority int unsigned default 0 comment 'Priority',
                        attachment varchar(128) default '' comment 'Attachment',
                        type int unsigned default 0 comment 'Type',
                        state int unsigned default 0 comment 'State',
                        createdTime datetime not null comment 'Created Time',
                        updatedTime datetime not null comment 'Updated Time',
                        dueTime datetime not null comment 'Due Time',
                        linkedTieketId int unsigned default 0 comment 'Linked Ticket ID',
                        lastEditedBy int unsigned default 0 comment 'Last Edited By',
                        constraint fk_article_user foreign key (ownerId) references users(id)
) comment 'Tickets';

create table category(
                         id int unsigned primary key auto_increment comment 'ID',
                         categoryName varchar(32) not null unique comment 'Category Name',
                         categoryDetail varchar(100) comment 'Category Detail',
                         ownerId int unsigned not null comment 'Owner ID',
                         createdTime datetime not null comment 'Created Time',
                         updatedTime datetime not null comment 'Updated Time',
                         constraint fk_category_user foreign key (ownerId) references users(id)
) comment "Category";