package com.example.testfinal.exceptions.impl.person;

public class WrongParametersException extends RuntimeException{
    public WrongParametersException(){
        super("Wrong parameters provided");
    }
}
