package com.example.testfinal.exceptions.impl.person;

public class PersonNotFoundException extends RuntimeException{
    public PersonNotFoundException(long id){
        super("Can't find person with id: " + id);
    }

}
