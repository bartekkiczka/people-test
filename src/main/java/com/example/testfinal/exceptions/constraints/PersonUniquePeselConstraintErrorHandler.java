package com.example.testfinal.exceptions.constraints;

import com.example.testfinal.enums.ConstraintErrorCode;
import com.example.testfinal.exceptions.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PersonUniquePeselConstraintErrorHandler implements ConstraintErrorHandler{
    @Override
    public ErrorMessage mapToErrorMessage() {
        return ErrorMessage.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .timeStamp(LocalDateTime.now())
                .message("Pesel is not unique")
                .description("/person")
                .build();
    }

    @Override
    public String getConstraintName() {
        return ConstraintErrorCode.UC_PERSON_PESEL.toString();
    }
}
