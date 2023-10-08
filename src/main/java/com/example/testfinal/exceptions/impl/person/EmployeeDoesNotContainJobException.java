package com.example.testfinal.exceptions.impl.person;

public class EmployeeDoesNotContainJobException extends RuntimeException{
    public EmployeeDoesNotContainJobException(long employeeId, long jobId){
        super("Employee with id: " + employeeId + " does not contain job with id: " + jobId);
    }
}
