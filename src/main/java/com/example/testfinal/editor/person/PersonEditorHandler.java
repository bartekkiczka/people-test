package com.example.testfinal.editor.person;

import com.example.testfinal.model.Person;
import com.example.testfinal.model.command.edit.EditPersonCommand;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class PersonEditorHandler {

    private final Map<String, PersonEditor> visitors;

    public PersonEditorHandler(Set<PersonEditor> visitors) {
        this.visitors = visitors.stream().collect(Collectors.toMap(PersonEditor::getType, Function.identity()));
    }

    public Person edit(Person person, EditPersonCommand command) {
        return visitors.get(person.getType().toString()).edit(person, command);
    }
}
