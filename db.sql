drop database sbQuickStart;

create database sbQuickStart;

use sbQuickStart;


create table users(
                      id int unsigned auto_increment primary key comment 'ID',
                      username varchar(20) not null unique comment 'Username',
                      password varchar(32) not null comment 'Password',
                      nickname varchar(20) default '' comment 'Nickname',
                      email varchar(100) unique comment 'Email',
                      avatar varchar(128) default '' comment 'Avatar',
                      status int unsigned default 5 comment 'Status',
                      belongsTo varchar(64) comment 'Belongs To',
                      created_time datetime not null comment 'Created Time',
                      updated_time datetime not null comment 'Updated Time'
) comment 'Users';

create table category(
                         id int unsigned primary key auto_increment comment 'ID',
                         categoryName varchar(32) not null unique comment 'Category Name',
                         categoryDetail varchar(100) comment 'Category Detail',
                         ownerId int unsigned not null comment 'Owner ID',
                         member varchar(128) default '' comment 'Member',
                         groupAdmin varchar(32) default '' comment 'Group Admin',
                         createdTime datetime not null comment 'Created Time',
                         updatedTime datetime not null comment 'Updated Time',
                         lastEditedBy int unsigned default 0 comment 'Last Edited By',
                         constraint fk_category_user foreign key (ownerId) references users(id)
) comment "Category";

create table tickets(
                        id int unsigned auto_increment primary key comment 'ID',
                        title varchar(100) not null comment 'Title',
                        description varchar(10000) not null comment 'Description',
                        ownerId int unsigned default 0 comment 'Owner ID',
                        assigneeId int unsigned default 0 comment 'Assignee ID',
                        watcherId varchar(32) default '' comment 'Watcher ID',
                        image varchar(512) default '' comment 'Image',
                        priority int unsigned default 5 comment 'Priority',
                        attachment varchar(512) default '' comment 'Attachment',
                        type int unsigned default 0 comment 'Type',
                        state int unsigned default 0 comment 'State',
                        createdTime datetime not null comment 'Created Time',
                        updatedTime datetime not null comment 'Updated Time',
                        dueTime datetime not null comment 'Due Time',
                        linkedTieketId int unsigned default 0 comment 'Linked Ticket ID',
                        belongsTo int unsigned default 0 comment 'Belongs To',
                        lastEditedBy int unsigned default 0 comment 'Last Edited By',
                        constraint fk_ticket_user foreign key (ownerId) references users(id),
                        constraint fk_ticket_group foreign key (belongsTo) references category(id)
) comment 'Tickets';


create table user_category(
                        userId int unsigned not null comment 'User ID',
                        categoryId int unsigned not null comment 'Category ID',
                        constraint fk_user_category_user foreign key (userId) references users(id),
                        constraint fk_user_category_category foreign key (categoryId) references category(id)
) comment 'User Category';

create table admin_category(
                               userId int unsigned not null comment 'User ID',
                               categoryId int unsigned not null comment 'Category ID',
                               constraint fk_admin_category_user foreign key (userId) references users(id),
                               constraint fk_admin_category_category foreign key (categoryId) references category(id)
) comment 'Admin Category';


create table ticket_watcher(
                        ticketId int unsigned not null comment 'Ticket ID',
                        userId int unsigned not null comment 'User ID',
                        constraint fk_ticket_watcher_ticket foreign key (ticketId) references tickets(id),
                        constraint fk_ticket_watcher_user foreign key (userId) references users(id)
) comment 'Ticket Watcher';

create table ticket_ticket(
                        ticketId int unsigned not null comment 'Ticket ID',
                        linkedTicketId int unsigned not null comment 'Linked Ticket ID',
                        constraint fk_ticket_ticket_ticket foreign key (ticketId) references tickets(id),
                        constraint fk_ticket_ticket_linked_ticket foreign key (linkedTicketId) references tickets(id)
) comment 'Ticket Ticket';


