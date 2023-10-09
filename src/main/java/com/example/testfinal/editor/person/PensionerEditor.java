package com.example.testfinal.editor.person;

import com.example.testfinal.model.Pensioner;
import com.example.testfinal.model.Person;
import com.example.testfinal.model.command.edit.EditPersonCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class PensionerEditor implements PersonEditor<Pensioner> {

    @Override
    public String getType() {
        return "PENSIONER";
    }

    @Override
    public Pensioner edit(Pensioner pensioner, EditPersonCommand command) {
        Map<String, Object> parameters = command.getParameters();
        String name = getStringParameters("name", parameters);
        String surname = getStringParameters("surname", parameters);
        long pesel = getLongParameters("pesel", parameters);
        int height = getIntegerParameters("height", parameters);
        int weight = getIntegerParameters("weight", parameters);
        String email = getStringParameters("email", parameters);
        int pension = getIntegerParameters("pension", parameters);
        int yearsWorked = getIntegerParameters("yearsWorked", parameters);

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
