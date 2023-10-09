package com.example.testfinal.editor.person;

import com.example.testfinal.model.Person;
import com.example.testfinal.model.command.edit.EditPersonCommand;

import java.util.Map;

public interface PersonEditor<T extends Person> {

    String getType();

    T edit(T person, EditPersonCommand command);

    default String getStringParameters(String name, Map<String, Object> parameters) {
        return (String) parameters.get(name);
    }

    default Integer getIntegerParameters(String name, Map<String, Object> parameters) {
        return (Integer) parameters.get(name);
    }

    default Long getLongParameters(String name, Map<String, Object> parameters) {
        return (Long) parameters.get(name);
    }
}
