package com.example.testfinal.exceptions.impl.job;

public class JobDatesOverlapException extends RuntimeException{
    public JobDatesOverlapException(){
        super("Position dates overlap with existing positions");
    }
}
