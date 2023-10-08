package com.example.testfinal.editor.person;

import com.example.testfinal.enums.PersonTypes;
import com.example.testfinal.model.Person;
import com.example.testfinal.model.Student;
import com.example.testfinal.model.command.edit.EditPersonCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class StudentEditor implements PersonEditor {

    @Override
    public String getType() {
        return PersonTypes.STUDENT.toString();
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
        String schoolName = getStringParameters("schoolName", parameters);
        int yearOfStudy = getIntegerParameters("yearOfStudy", parameters);
        String fieldOfStudy = getStringParameters("fieldOfStudy", parameters);
        int scholarship = getIntegerParameters("scholarship", parameters);

        Student student = (Student) person;
        student.setName(name);
        student.setSurname(surname);
        student.setPesel(pesel);
        student.setHeight(height);
        student.setWeight(weight);
        student.setEmail(email);
        student.setSchoolName(schoolName);
        student.setYearOfStudy(yearOfStudy);
        student.setFieldOfStudy(fieldOfStudy);
        student.setScholarship(scholarship);

        return student;

    }
}
