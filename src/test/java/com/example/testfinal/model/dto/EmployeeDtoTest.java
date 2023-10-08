package com.example.testfinal.model.dto;

import com.example.testfinal.model.Employee;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
public class EmployeeDtoTest {

    @Autowired
    private ModelMapper modelMapper;

    @Test
    public void testEmployeeToEmployeeDtoConversion() {
        Employee employee = Employee.builder()
                .name("name")
                .surname("surname")
                .pesel(66553315782L)
                .height(60)
                .weight(40)
                .email("employee@mail.com")
                .employmentStartDate(LocalDate.of(2021, 2, 2))
                .position("position")
                .salary(200)
                .build();

        EmployeeDto employeeDto = modelMapper.map(employee, EmployeeDto.class);

        assertEquals(employeeDto.getType(), employee.getType());
        assertEquals(employeeDto.getName(), employee.getName());
        assertEquals(employeeDto.getSurname(), employee.getSurname());
        assertEquals(employeeDto.getPesel(), employee.getPesel());
        assertEquals(employeeDto.getHeight(), employee.getHeight());
        assertEquals(employeeDto.getWeight(), employee.getWeight());
        assertEquals(employeeDto.getEmail(), employee.getEmail());
        assertEquals(employeeDto.getEmploymentStartDate(), employee.getEmploymentStartDate());
        assertEquals(employeeDto.getPosition(), employee.getPosition());
        assertEquals(employeeDto.getSalary(), employee.getSalary());
    }
}
