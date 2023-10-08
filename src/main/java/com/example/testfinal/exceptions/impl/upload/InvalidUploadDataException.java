package com.example.testfinal.exceptions.impl.upload;

public class InvalidUploadDataException extends RuntimeException{
    public InvalidUploadDataException(){
        super("Provided invalid data format");
    }
}
