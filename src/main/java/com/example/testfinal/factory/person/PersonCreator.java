package com.example.testfinal.factory.person;

import com.example.testfinal.model.Person;

import java.util.Map;

public interface PersonCreator {
    String getType();
    Person create(Map<String, Object> parameters);

    default String getStringParameters(String name, Map<String, Object> parameters){
        return (String) parameters.get(name);
    }

    default Integer getIntegerParameters(String name, Map<String, Object> parameters){
        return (Integer) parameters.get(name);
    }

    default Long getLongParameters(String name, Map<String, Object> parameters){
        return (Long) parameters.get(name);
    }
}
