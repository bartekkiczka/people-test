package com.example.testfinal.exceptions.impl.person;

public class PersonMappingException extends RuntimeException{
    public PersonMappingException(){
        super("Can't map to a person");
    }
}
