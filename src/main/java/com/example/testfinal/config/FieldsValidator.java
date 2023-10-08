package com.example.testfinal.config;

import com.example.testfinal.exceptions.impl.validation.ValidationException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Component
public class FieldsValidator {

    private final Validator validator;

    public <T> void validateFields(T obj){
        Set<ConstraintViolation<T>> violations = validator.validate(obj);
        if (!violations.isEmpty()) {
            List<String> errorMessages = new ArrayList<>();
            for (ConstraintViolation<T> violation : violations) {
                errorMessages.add(violation.getMessage());
            }
            throw new ValidationException(errorMessages);
        }
    }
}
