package com.example.testfinal.exceptions.impl.job;

public class JobAlreadyAssignedException extends RuntimeException{
    public JobAlreadyAssignedException(){
        super("This job is already assigned");
    }
}
