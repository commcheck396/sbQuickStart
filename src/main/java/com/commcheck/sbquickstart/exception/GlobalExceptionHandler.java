package com.commcheck.sbquickstart.exception;

import com.commcheck.sbquickstart.pojo.Result;
import com.mysql.cj.util.StringUtils;
import lombok.experimental.ExtensionMethod;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e) {
        e.printStackTrace();
        return Result.fail((e.getMessage()).length()>1 ? e.getMessage() : "Operation failed.");
    }

}
