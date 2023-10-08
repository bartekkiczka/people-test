package com.example.testfinal.exceptions.impl.person;

public class PersonTypeNotExistException extends RuntimeException{
    public PersonTypeNotExistException(String type){
        super("Person of type " + type + " don't exist");
    }
}
