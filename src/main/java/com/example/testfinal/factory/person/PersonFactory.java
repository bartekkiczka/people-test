package com.example.testfinal.factory.person;

import com.example.testfinal.exceptions.impl.person.PersonTypeNotExistException;
import com.example.testfinal.model.Person;
import com.example.testfinal.model.command.create.CreatePersonCommand;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class PersonFactory {

    private final Map<String, PersonCreator> creators; 

    public PersonFactory(Set<PersonCreator> creators){
        this.creators = creators.stream().collect(Collectors.toMap(PersonCreator::getType, Function.identity()));
    }

    public Person create(CreatePersonCommand command) {
        return Optional.ofNullable(creators.get(command.getType()))
                .map(creator -> creator.create(command.getParameters()))
                .orElseThrow(() -> new PersonTypeNotExistException(command.getType()));
    }
}
