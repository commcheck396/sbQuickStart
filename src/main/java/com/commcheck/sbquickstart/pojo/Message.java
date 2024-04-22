package com.commcheck.sbquickstart.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Message {
    private Integer id;
    private Integer sender;
    private Integer receiver;
    private Integer target;
    private Integer type;
    private Integer status;
    private String message;
    private String createdTime;
    private String updatedTime;
    private Integer lastEditedBy;
}