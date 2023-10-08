package com.example.testfinal.exceptions.impl.job;

public class JobNotFoundException extends RuntimeException{
    public JobNotFoundException(long id){
        super("Can't find job with id: " + id);
    }
}
