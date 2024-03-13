package com.commcheck.sbquickstart.pojo;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
@Data
public class Category {
    @NotNull
    private Integer id;
    @NotEmpty(groups = Add.class)
    @Pattern(regexp = "^[a-zA-Z0-9_-]{3,30}$", message = "illinegal category name")
    private String categoryName;
    private String categoryDetail;
    private Integer ownerId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createdTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime updatedTime;

    public interface Add{}

}
