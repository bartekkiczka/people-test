package com.example.testfinal.editor.person;

import com.example.testfinal.enums.PersonTypes;
import com.example.testfinal.model.Pensioner;
import com.example.testfinal.model.Person;
import com.example.testfinal.model.command.edit.EditPersonCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class PensionerEditor implements PersonEditor {

    @Override
    public String getType() {
        return PersonTypes.PENSIONER.toString();
    }

    @Override
    public Person edit(Person person, EditPersonCommand command) {
        Map<String, Object> parameters = command.getParameters();
        String name = getStringParameters("name", parameters);
        String surname = getStringParameters("surname", parameters);
        long pesel = getLongParameters("pesel", parameters);
        int height = getIntegerParameters("height", parameters);
        int weight = getIntegerParameters("weight", parameters);
        String email = getStringParameters("email", parameters);
        int pension = getIntegerParameters("pension", parameters);
        int yearsWorked = getIntegerParameters("yearsWorked", parameters);

        Pensioner pensioner = (Pensioner) person;
        pensioner.setName(name);
        pensioner.setSurname(surname);
        pensioner.setPesel(pesel);
        pensioner.setHeight(height);
        pensioner.setWeight(weight);
        pensioner.setEmail(email);
        pensioner.setYearsWorked(yearsWorked);
        pensioner.setPension(pension);

        return pensioner;
    }
}
