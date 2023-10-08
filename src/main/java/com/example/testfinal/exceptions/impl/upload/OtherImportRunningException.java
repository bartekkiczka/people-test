package com.example.testfinal.exceptions.impl.upload;

public class OtherImportRunningException extends RuntimeException{
    public OtherImportRunningException(){
        super("Another import is already in progress. Can't start a new one");
    }
}
