package com.commcheck.sbquickstart.validation;

import com.commcheck.sbquickstart.anno.URL;
import com.commcheck.sbquickstart.utils.SplitUtil;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;

public class URLValidation implements ConstraintValidator<URL, String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        List<String> URLList = SplitUtil.splitBySemicolon(s);
        for (String url : URLList) {
            if (!isValidURL(url)) {
                return false;
            }
        }
        return true;
    }

    private boolean isValidURL(String s) {
        // Logic to validate URL
        return s.startsWith("http://") || s.startsWith("https://");
    }
}
