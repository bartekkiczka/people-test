package com.example.testfinal.queryBuilder.search.mappers;

import com.example.testfinal.model.Employee;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.sql.ResultSet;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
class EmployeeMapperTest {

    @Autowired
    private EmployeeMapper employeeMapper;


    @Test
    public void testMapEmployee() throws Exception {
        ResultSet resultSet = mock(ResultSet.class);

        when(resultSet.getLong("id")).thenReturn(1L);
        when(resultSet.getString("name")).thenReturn("John");
        when(resultSet.getString("surname")).thenReturn("Doe");
        when(resultSet.getLong("pesel")).thenReturn(1234567890L);
        when(resultSet.getInt("height")).thenReturn(180);
        when(resultSet.getInt("weight")).thenReturn(75);
        when(resultSet.getString("email")).thenReturn("john@example.com");
        when(resultSet.getString("employment_start_date")).thenReturn("2023-09-19");
        when(resultSet.getString("position")).thenReturn("Software Engineer");
        when(resultSet.getInt("salary")).thenReturn(1000);

        Employee employee = (Employee) employeeMapper.map(resultSet);

        assertEquals(1L, employee.getId());
        assertEquals("John", employee.getName());
        assertEquals("Doe", employee.getSurname());
        assertEquals(1234567890L, employee.getPesel());
        assertEquals(180, employee.getHeight());
        assertEquals(75, employee.getWeight());
        assertEquals("john@example.com", employee.getEmail());
        assertEquals(LocalDate.parse("2023-09-19"), employee.getEmploymentStartDate());
        assertEquals("Software Engineer", employee.getPosition());
        assertEquals(1000, employee.getSalary());
    }
}