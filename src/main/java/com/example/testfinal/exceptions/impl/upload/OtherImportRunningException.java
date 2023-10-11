package com.example.testfinal.exceptions.impl.upload;

public class OtherImportRunningException extends RuntimeException{
    public OtherImportRunningException(){
        super("Another import is already in progress. Your request has been added to queue");
    }
}
