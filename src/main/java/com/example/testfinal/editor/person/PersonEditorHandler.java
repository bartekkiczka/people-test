package com.example.testfinal.editor.person;

import com.example.testfinal.model.Person;
import com.example.testfinal.model.command.edit.EditPersonCommand;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class PersonEditorHandler<T extends Person> {

    private final Map<String, PersonEditor<T>> editors;

    public PersonEditorHandler(Set<PersonEditor<T>> visitors) {
        this.editors = visitors.stream().collect(Collectors.toMap(PersonEditor::getType, Function.identity()));
    }

    public T edit(T person, EditPersonCommand command) {
        return editors.get(person.getType()).edit(person, command);
    }
}
