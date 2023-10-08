package com.example.testfinal.exceptions.impl.validation;

public class ParameterNotExistsException extends RuntimeException{
    public ParameterNotExistsException(){
        super("Provided parameter does not exists");
    }
}
