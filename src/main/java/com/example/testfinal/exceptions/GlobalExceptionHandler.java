package com.example.testfinal.exceptions;

import com.example.testfinal.exceptions.constraints.ConstraintErrorHandler;
import com.example.testfinal.exceptions.impl.job.JobAlreadyAssignedException;
import com.example.testfinal.exceptions.impl.job.JobDatesOverlapException;
import com.example.testfinal.exceptions.impl.job.JobEndDateIsBeforeStartDateException;
import com.example.testfinal.exceptions.impl.job.JobNotFoundException;
import com.example.testfinal.exceptions.impl.person.*;
import com.example.testfinal.exceptions.impl.upload.ImportStatusNotFoundException;
import com.example.testfinal.exceptions.impl.upload.OtherImportRunningException;
import com.example.testfinal.exceptions.impl.validation.ParameterNotExistsException;
import com.example.testfinal.exceptions.impl.validation.ValidationException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    private Map<String, ConstraintErrorHandler> constraintErrorHandler;

    public GlobalExceptionHandler(Set<ConstraintErrorHandler> handlers) {
        this.constraintErrorHandler = handlers.stream()
                .collect(Collectors.toMap(ConstraintErrorHandler::getConstraintName, Function.identity()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorMessage> handleConstraintViolationException(final ConstraintViolationException e, WebRequest request) {
        String constraintName = e.getConstraintName();
        return new ResponseEntity<>(constraintErrorHandler.get(constraintName.toUpperCase()).mapToErrorMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorMessage> handleValidationException(final ValidationException e, WebRequest request) {
        return new ResponseEntity<>(ErrorMessage.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .timeStamp(LocalDateTime.now())
                .message(e.getMessage())
                .description(request.getDescription(false))
                .build(),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PersonNotFoundException.class)
    public ResponseEntity<ErrorMessage> handlePersonNotFoundException(final PersonNotFoundException e, WebRequest request) {
        return new ResponseEntity<>(ErrorMessage.builder()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .timeStamp(LocalDateTime.now())
                .message(e.getMessage())
                .description(request.getDescription(false))
                .build(),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PersonIsNotEmployeeException.class)
    public ResponseEntity<ErrorMessage> handlePersonIsNotEmployeeException(final PersonIsNotEmployeeException e, WebRequest request) {
        return new ResponseEntity<>(ErrorMessage.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .timeStamp(LocalDateTime.now())
                .message(e.getMessage())
                .description(request.getDescription(false))
                .build(),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(JobDatesOverlapException.class)
    public ResponseEntity<ErrorMessage> handleJobDatesOverlapException(final JobDatesOverlapException e, WebRequest request) {
        return new ResponseEntity<>(ErrorMessage.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .timeStamp(LocalDateTime.now())
                .message(e.getMessage())
                .description(request.getDescription(false))
                .build(),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(OtherImportRunningException.class)
    public ResponseEntity<ErrorMessage> handleOtherImportRunningException(final OtherImportRunningException e, WebRequest request) {
        return new ResponseEntity<>(ErrorMessage.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .timeStamp(LocalDateTime.now())
                .message(e.getMessage())
                .description(request.getDescription(false))
                .build(),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PersonTypeNotExistException.class)
    public ResponseEntity<ErrorMessage> handlePersonTypeNotExistException(final PersonTypeNotExistException e, WebRequest request) {
        return new ResponseEntity<>(ErrorMessage.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .timeStamp(LocalDateTime.now())
                .message(e.getMessage())
                .description(request.getDescription(false))
                .build(),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ImportStatusNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleImportStatusNotFoundException(final ImportStatusNotFoundException e, WebRequest request) {
        return new ResponseEntity<>(ErrorMessage.builder()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .timeStamp(LocalDateTime.now())
                .message(e.getMessage())
                .description(request.getDescription(false))
                .build(),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(JobEndDateIsBeforeStartDateException.class)
    public ResponseEntity<ErrorMessage> handleJobEndDateIsBeforeStartDateException(final JobEndDateIsBeforeStartDateException e, WebRequest request) {
        return new ResponseEntity<>(ErrorMessage.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .timeStamp(LocalDateTime.now())
                .message(e.getMessage())
                .description(request.getDescription(false))
                .build(),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(JobNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleJobNotFoundException(final JobNotFoundException e, WebRequest request) {
        return new ResponseEntity<>(ErrorMessage.builder()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .timeStamp(LocalDateTime.now())
                .message(e.getMessage())
                .description(request.getDescription(false))
                .build(),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(JobAlreadyAssignedException.class)
    public ResponseEntity<ErrorMessage> handleJobAlreadyAssignedException(final JobAlreadyAssignedException e, WebRequest request) {
        return new ResponseEntity<>(ErrorMessage.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .timeStamp(LocalDateTime.now())
                .message(e.getMessage())
                .description(request.getDescription(false))
                .build(),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidOrNoParameterProvidedException.class)
    public ResponseEntity<ErrorMessage> handleInvalidOrNoParameterProvidedException(final InvalidOrNoParameterProvidedException e, WebRequest request) {
        return new ResponseEntity<>(ErrorMessage.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .timeStamp(LocalDateTime.now())
                .message(e.getMessage())
                .description(request.getDescription(false))
                .build(),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmployeeDoesNotContainJobException.class)
    public ResponseEntity<ErrorMessage> handleEmployeeDoesNotContainJobException(final EmployeeDoesNotContainJobException e, WebRequest request) {
        return new ResponseEntity<>(ErrorMessage.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .timeStamp(LocalDateTime.now())
                .message(e.getMessage())
                .description(request.getDescription(false))
                .build(),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ParameterNotExistsException.class)
    public ResponseEntity<ErrorMessage> handleColumnNotExistsException(final ParameterNotExistsException e, WebRequest request) {
        return new ResponseEntity<>(ErrorMessage.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .timeStamp(LocalDateTime.now())
                .message(e.getMessage())
                .description(request.getDescription(false))
                .build(),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmployeeContainsJobsException.class)
    public ResponseEntity<ErrorMessage> handleEmployeeContainsJobsException(final EmployeeContainsJobsException e, WebRequest request) {
        return new ResponseEntity<>(ErrorMessage.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .timeStamp(LocalDateTime.now())
                .message(e.getMessage())
                .description(request.getDescription(false))
                .build(),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(WrongParametersException.class)
    public ResponseEntity<ErrorMessage> handleWrongParametersException(final WrongParametersException e, WebRequest request) {
        return new ResponseEntity<>(ErrorMessage.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .timeStamp(LocalDateTime.now())
                .message(e.getMessage())
                .description(request.getDescription(false))
                .build(),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PersonMappingException.class)
    public ResponseEntity<ErrorMessage> handlePersonMappingException(final PersonMappingException e, WebRequest request) {
        return new ResponseEntity<>(ErrorMessage.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .timeStamp(LocalDateTime.now())
                .message(e.getMessage())
                .description(request.getDescription(false))
                .build(),
                HttpStatus.BAD_REQUEST);
    }
}
