package com.commcheck.sbquickstart.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class User {
    private Integer id;
    private String username;
    @JsonIgnore
    private String password;
    private String nickname;
    @Email
    private String email;
    private String avatar;
    private Integer status;
    private LocalDateTime created_time;
    private LocalDateTime updated_time;
    private String belongsTo;
}
