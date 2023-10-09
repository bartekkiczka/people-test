package com.example.testfinal.factory.person;

import com.example.testfinal.model.Employee;
import com.example.testfinal.model.Pensioner;
import com.example.testfinal.model.Person;
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
class PersonFactoryTest {

    @Autowired
    private PersonFactory personFactory;

    @Autowired
    private ModelMapper modelMapper;

    @Test
    public void testPersonFactoryShouldCreatePensioner() {
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
                .type("PENSIONER")
                .parameters(params)
                .build();

        Person person = personFactory.create(createPersonCommand);
        Pensioner pensioner = modelMapper.map(person, Pensioner.class);

        assertEquals(pensioner.getType(), "PENSIONER");
        assertEquals(pensioner.getName(), "name");
        assertEquals(pensioner.getSurname(), "surname");
        assertEquals(pensioner.getPesel(), 44556677889L);
        assertEquals(pensioner.getHeight(), 50);
        assertEquals(pensioner.getWeight(), 50);
        assertEquals(pensioner.getEmail(), "test@abc.com");
        assertEquals(pensioner.getYearsWorked(), 30);
        assertEquals(pensioner.getPension(), 300);
    }

    @Test
    public void testPersonFactoryShouldCreateEmployee() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "name");
        params.put("surname", "surname");
        params.put("pesel", 44556677889L);
        params.put("height", 50);
        params.put("weight", 50);
        params.put("email", "test@abc.com");
        params.put("employmentStartDate", "01-01-2020");
        params.put("position", "pos");
        params.put("salary", 100);

        CreatePersonCommand createPersonCommand = CreatePersonCommand.builder()
                .type("EMPLOYEE")
                .parameters(params)
                .build();

        Person person = personFactory.create(createPersonCommand);
        Employee employee = modelMapper.map(person, Employee.class);

        assertEquals(employee.getType(), "EMPLOYEE");
        assertEquals(employee.getName(), "name");
        assertEquals(employee.getSurname(), "surname");
        assertEquals(employee.getPesel(), 44556677889L);
        assertEquals(employee.getHeight(), 50);
        assertEquals(employee.getWeight(), 50);
        assertEquals(employee.getEmail(), "test@abc.com");
        assertEquals(employee.getEmploymentStartDate(), LocalDate.of(2020, 1, 1));
        assertEquals(employee.getPosition(), "pos");
        assertEquals(employee.getSalary(), 100);
    }


    @Test
    public void testPersonFactoryShouldCreateStudent() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "name");
        params.put("surname", "surname");
        params.put("pesel", 44556677889L);
        params.put("height", 50);
        params.put("weight", 50);
        params.put("email", "test@abc.com");
        params.put("schoolName", "school");
        params.put("yearOfStudy", 5);
        params.put("fieldOfStudy", "field");
        params.put("scholarship", 300);

        CreatePersonCommand createPersonCommand = CreatePersonCommand.builder()
                .type("STUDENT")
                .parameters(params)
                .build();

        Person person = personFactory.create(createPersonCommand);
        Student student = modelMapper.map(person, Student.class);

        assertEquals(student.getType(), "STUDENT");
        assertEquals(student.getName(), "name");
        assertEquals(student.getSurname(), "surname");
        assertEquals(student.getPesel(), 44556677889L);
        assertEquals(student.getHeight(), 50);
        assertEquals(student.getWeight(), 50);
        assertEquals(student.getEmail(), "test@abc.com");
        assertEquals(student.getSchoolName(), "school");
        assertEquals(student.getYearOfStudy(), 5);
        assertEquals(student.getFieldOfStudy(), "field");
        assertEquals(student.getScholarship(), 300);
    }

}