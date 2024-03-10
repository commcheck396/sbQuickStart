package com.commcheck.sbquickstart.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Result<T> {
    private Integer code;
    private String message;
    private T data;

    public static <E> Result<E> success(E data) {
        return new Result<>(0, "Operation success", data);
    }

    public static Result success () {
        return new Result(0, "Operation success", null);
    }

    public static Result success (String message) {
        return new Result(0, message, null);
    }

    public static Result fail (String message) {
        return new Result(1, message, null);
    }
}
