package com.commcheck.sbquickstart.anno;

import com.commcheck.sbquickstart.validation.URLValidation;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(
        validatedBy = {URLValidation.class}
)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)

public @interface URL {
    String message() default "{Must be a valid URL series devide by ;}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
