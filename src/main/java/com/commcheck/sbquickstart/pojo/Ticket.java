package com.commcheck.sbquickstart.pojo;

//SQL:
//create table tickets(
//        id int unsigned auto_increment primary key comment 'ID',
//        title varchar(100) not null comment 'Title',
//        description varchar(10000) not null comment 'Description',
//        owner_id int unsigned default 0 comment 'Owner ID',
//        assignee_id int unsigned default 0 comment 'Assignee ID',
//        watcher_id varchar(1000) default '' comment 'Watcher ID',
//        cover varchar(128) default '' comment 'Cover',
//        priority int unsigned default 0 comment 'Priority',
//        attachment varchar(128) default '' comment 'Attachment',
//        type int unsigned default 0 comment 'Type',
//        state int unsigned default 0 comment 'State',
//        created_time datetime not null comment 'Created Time',
//        updated_time datetime not null comment 'Updated Time',
//        due_time datetime not null comment 'Due Time',
//        linked_tieket_id int unsigned default 0 comment 'Linked Ticket ID',
//        constraint fk_article_user foreign key (owner_id) references users(id)
//        ) comment 'Tickets';

import com.commcheck.sbquickstart.anno.URL;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class Ticket {
    private Integer id;
    @NotEmpty
    @Pattern(regexp = "^[a-zA-Z0-9]{1,100}$", message = "Title must be 1-100 characters long and only contain letters and numbers.")
    private String title;
    @NotEmpty
    private String description;
    private Integer ownerId;
    private Integer assigneeId;
    private String watcherId;
    @URL
    private String image;
    private Integer priority;
    @URL
    private String attachment;
    private Integer type;
    private Integer state;
    private String createdTime;
    private String updatedTime;
    private String dueTime;
    private Integer linkedTicketId;
    private Integer lastEditedBy;
    @NotEmpty
    private Integer belongsTo;
}
