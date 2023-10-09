package com.example.testfinal.model.command.create;

import com.example.testfinal.model.Employee;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class CreateEmployeeCommandTest {

    @Autowired
    private ModelMapper modelMapper;

    @Test
    public void testCreateEmployeeCommandToEmployeeConversion(){
        //given
        CreateEmployeeCommand command = CreateEmployeeCommand.builder()
                .type("EMPLOYEE")
                .name("name")
                .surname("surname")
                .pesel(11223344556L)
                .height(30)
                .weight(30)
                .email("testemployee@gmail.com")
                .employmentStartDate(LocalDate.of(2020,1,1))
                .position("position")
                .salary(100)
                .build();

        //when
        Employee employee = modelMapper.map(command, Employee.class);

        //then
        assertEquals(employee.getType(), "EMPLOYEE");
        assertEquals(employee.getName(), "name");
        assertEquals(employee.getSurname(), "surname");
        assertEquals(employee.getPesel(), 11223344556L);
        assertEquals(employee.getHeight(), 30);
        assertEquals(employee.getWeight(), 30);
        assertEquals(employee.getEmail(), "testemployee@gmail.com");
        assertEquals(employee.getEmploymentStartDate(), LocalDate.of(2020,1,1));
        assertEquals(employee.getPosition(), "position");
        assertEquals(employee.getSalary(), 100);
    }
}