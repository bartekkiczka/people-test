package com.example.testfinal.exceptions.impl.person;

public class PersonIsNotEmployeeException extends RuntimeException{
    public PersonIsNotEmployeeException(long id){
        super("Person with id " + id + " is not an Employee");
    }
}
