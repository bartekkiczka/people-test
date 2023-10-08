package com.example.testfinal.factory.person.creators;

import com.example.testfinal.config.FieldsValidator;
import com.example.testfinal.enums.PersonTypes;
import com.example.testfinal.factory.person.PersonCreator;
import com.example.testfinal.model.Pensioner;
import com.example.testfinal.model.Person;
import com.example.testfinal.model.command.create.CreatePensionerCommand;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class PensionerCreator implements PersonCreator {

    private final ModelMapper modelMapper;
    private final FieldsValidator fieldsValidator;

    @Override
    public String getType() {
        return PersonTypes.PENSIONER.toString();
    }

    @Override
    public Person create(Map<String, Object> parameters) {
        PersonTypes type = PersonTypes.PENSIONER;
        String name = getStringParameters("name", parameters);
        String surname = getStringParameters("surname", parameters);
        long pesel = getLongParameters("pesel", parameters);
        int height = getIntegerParameters("height", parameters);
        int weight = getIntegerParameters("weight", parameters);
        String email = getStringParameters("email", parameters);
        int pension = getIntegerParameters("pension", parameters);
        int yearsWorked = getIntegerParameters("yearsWorked", parameters);
        CreatePensionerCommand command = CreatePensionerCommand
                .builder()
                .type(type)
                .name(name)
                .surname(surname)
                .pesel(pesel)
                .height(height)
                .weight(weight)
                .email(email)
                .pension(pension)
                .yearsWorked(yearsWorked)
                .build();

        fieldsValidator.validateFields(command);

        return modelMapper.map(command, Pensioner.class);
    }
}
