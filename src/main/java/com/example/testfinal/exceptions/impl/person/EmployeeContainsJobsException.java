package com.example.testfinal.exceptions.impl.person;

public class EmployeeContainsJobsException extends RuntimeException{
    public EmployeeContainsJobsException(){
        super("Can't delete employee because he still contains jobs. Remove them first");
    }
}
