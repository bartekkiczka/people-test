package com.example.testfinal.exceptions.impl.upload;

public class ImportStatusNotFoundException extends RuntimeException{
    public ImportStatusNotFoundException(){
        super("Import status not found");
    }
}
