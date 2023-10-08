package com.example.testfinal.factory.person.creators;

import com.example.testfinal.config.FieldsValidator;
import com.example.testfinal.enums.PersonTypes;
import com.example.testfinal.factory.person.PersonCreator;
import com.example.testfinal.model.Person;
import com.example.testfinal.model.Student;
import com.example.testfinal.model.command.create.CreateStudentCommand;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class StudentCreator implements PersonCreator {

    private final ModelMapper modelMapper;
    private final FieldsValidator fieldsValidator;

    @Override
    public String getType() {
        return PersonTypes.STUDENT.toString();
    }

    @Override
    public Person create(Map<String, Object> parameters) {
        PersonTypes type = PersonTypes.STUDENT;
        String name = getStringParameters("name", parameters);
        String surname = getStringParameters("surname", parameters);
        long pesel = getLongParameters("pesel", parameters);
        int height = getIntegerParameters("height", parameters);
        int weight = getIntegerParameters("weight", parameters);
        String email = getStringParameters("email", parameters);
        String schoolName = getStringParameters("schoolName", parameters);
        int yearOfStudy = getIntegerParameters("yearOfStudy", parameters);
        String fieldOfStudy = getStringParameters("fieldOfStudy", parameters);
        int scholarship = getIntegerParameters("scholarship", parameters);
        CreateStudentCommand command = CreateStudentCommand.builder()
                .type(type)
                .name(name)
                .surname(surname)
                .pesel(pesel)
                .height(height)
                .weight(weight)
                .email(email)
                .schoolName(schoolName)
                .yearOfStudy(yearOfStudy)
                .fieldOfStudy(fieldOfStudy)
                .scholarship(scholarship)
                .build();

        fieldsValidator.validateFields(command);

        return modelMapper.map(command, Student.class);
    }
}
