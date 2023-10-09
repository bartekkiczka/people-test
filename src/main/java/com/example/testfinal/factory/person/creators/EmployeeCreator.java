package com.example.testfinal.factory.person.creators;

import com.example.testfinal.config.FieldsValidator;
import com.example.testfinal.factory.person.PersonCreator;
import com.example.testfinal.model.Employee;
import com.example.testfinal.model.Person;
import com.example.testfinal.model.command.create.CreateEmployeeCommand;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmployeeCreator implements PersonCreator {

    private final ModelMapper modelMapper;
    private final FieldsValidator fieldsValidator;

    @Override
    public String getType() {
        return "EMPLOYEE";
    }

    @Override
    public Person create(Map<String, Object> parameters) {
        String type = "EMPLOYEE";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String name = getStringParameters("name", parameters);
        String surname = getStringParameters("surname", parameters);
        long pesel = getLongParameters("pesel", parameters);
        int height = getIntegerParameters("height", parameters);
        int weight = getIntegerParameters("weight", parameters);
        String email = getStringParameters("email", parameters);
        String employmentStartDateString = getStringParameters("employmentStartDate", parameters);
        LocalDate employmentStartDate = LocalDate.parse(employmentStartDateString, formatter);
        String positions = getStringParameters("position", parameters);
        int salary = getIntegerParameters("salary", parameters);

        CreateEmployeeCommand command = CreateEmployeeCommand.builder()
                .type(type)
                .name(name)
                .surname(surname)
                .pesel(pesel)
                .height(height)
                .weight(weight)
                .email(email)
                .employmentStartDate(employmentStartDate)
                .position(positions)
                .salary(salary)
                .build();

        fieldsValidator.validateFields(command);

        return modelMapper.map(command, Employee.class);
    }
}
