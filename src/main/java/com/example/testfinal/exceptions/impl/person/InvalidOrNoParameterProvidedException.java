package com.example.testfinal.exceptions.impl.person;

public class InvalidOrNoParameterProvidedException extends RuntimeException{
    public InvalidOrNoParameterProvidedException(){
        super("Invalid or no parameter provided");
    }
}
