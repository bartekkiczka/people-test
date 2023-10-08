package com.example.testfinal.exceptions.impl.job;

public class JobEndDateIsBeforeStartDateException extends RuntimeException{
    public JobEndDateIsBeforeStartDateException(){
        super("Job end date is before start date");
    }
}
