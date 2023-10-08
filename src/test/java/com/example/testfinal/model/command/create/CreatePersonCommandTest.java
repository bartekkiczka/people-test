package com.example.testfinal.model.command.create;

import com.example.testfinal.enums.PersonTypes;
import com.example.testfinal.model.Employee;
import com.example.testfinal.model.Pensioner;
import com.example.testfinal.model.Student;
import com.example.testfinal.model.command.create.CreatePersonCommand;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class CreatePersonCommandTest {

    @Autowired
    private ModelMapper modelMapper;

    @Test
    public void createPersonCommandShouldCreatePensionerInstance(){
        Map<String, Object> params = new HashMap<>();
        params.put("name", "name");
        params.put("surname", "surname");
        params.put("pesel", 44556677889L);
        params.put("height", 50);
        params.put("weight", 50);
        params.put("email", "test@abc.com");
        params.put("pension", 300);
        params.put("yearsWorked", 30);

        CreatePersonCommand createPersonCommand = CreatePersonCommand.builder()
                .type(PersonTypes.PENSIONER.toString())
                .parameters(params)
                .build();

        Pensioner person = modelMapper.map(createPersonCommand, Pensioner.class);

        assertInstanceOf(Pensioner.class, person);
    }

    @Test
    public void createPersonCommandShouldCreateStudentInstance(){
        Map<String, Object> params = new HashMap<>();
        params.put("name", "name");
        params.put("surname", "surname");
        params.put("pesel", 44556677889L);
        params.put("height", 50);
        params.put("weight", 50);
        params.put("email", "test@abc.com");
        params.put("schoolName", "school");
        params.put("fieldOfStudy", "field");
        params.put("yearOfStudy", 3);
        params.put("scholarship", 200);

        CreatePersonCommand createPersonCommand = CreatePersonCommand.builder()
                .type(PersonTypes.STUDENT.toString())
                .parameters(params)
                .build();

        Student person = modelMapper.map(createPersonCommand, Student.class);

        assertInstanceOf(Student.class, person);
    }

    @Test
    public void createPersonCommandShouldCreateEmployeeInstance(){
        Map<String, Object> params = new HashMap<>();
        params.put("name", "name");
        params.put("surname", "surname");
        params.put("pesel", 44556677889L);
        params.put("height", 50);
        params.put("weight", 50);
        params.put("email", "test@abc.com");
        params.put("employmentStartDate", LocalDate.of(2020,1,1));
        params.put("position", "position");
        params.put("salary", 3000);

        CreatePersonCommand createPersonCommand = CreatePersonCommand.builder()
                .type(PersonTypes.EMPLOYEE.toString())
                .parameters(params)
                .build();

        Employee person = modelMapper.map(createPersonCommand, Employee.class);

        assertInstanceOf(Employee.class, person);
    }

}