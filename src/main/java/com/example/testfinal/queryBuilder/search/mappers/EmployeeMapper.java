package com.example.testfinal.queryBuilder.search.mappers;

import com.example.testfinal.model.Employee;
import com.example.testfinal.model.Person;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.time.LocalDate;

@Service
public class EmployeeMapper implements PersonMapper{
    @Override
    public String getType() {
        return "EMPLOYEE";
    }

    @Override
    public Person map(ResultSet rs) throws Exception {
        long id = rs.getLong("id");
        String name = rs.getString("name");
        String surname = rs.getString("surname");
        long pesel = rs.getLong("pesel");
        int height = rs.getInt("height");
        int weight = rs.getInt("weight");
        String email = rs.getString("email");
        LocalDate employmentStartDate = LocalDate.parse(rs.getString("employment_start_date"));
        String position = rs.getString("position");
        int salary = rs.getInt("salary");

        Employee employee = new Employee();
        employee.setId(id);
        employee.setName(name);
        employee.setSurname(surname);
        employee.setPesel(pesel);
        employee.setHeight(height);
        employee.setWeight(weight);
        employee.setEmail(email);
        employee.setEmploymentStartDate(employmentStartDate);
        employee.setPosition(position);
        employee.setSalary(salary);
        return employee;
    }
}
