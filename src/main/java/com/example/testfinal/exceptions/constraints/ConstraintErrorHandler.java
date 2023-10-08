package com.example.testfinal.exceptions.constraints;

import com.example.testfinal.exceptions.ErrorMessage;

public interface ConstraintErrorHandler {
    ErrorMessage mapToErrorMessage();
    String getConstraintName();
}
