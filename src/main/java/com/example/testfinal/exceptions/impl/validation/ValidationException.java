package com.example.testfinal.exceptions.impl.validation;

import lombok.Getter;

import java.util.List;

@Getter
public class ValidationException extends RuntimeException{

    private final List<String> errors;

    public ValidationException(List<String> errors){
        this.errors = errors;
    }

    @Override
    public String getMessage(){
        StringBuilder msg = new StringBuilder();
        for(String s : errors){
            msg.append(s).append(", ");
        }
        return msg.toString();
    }
}
