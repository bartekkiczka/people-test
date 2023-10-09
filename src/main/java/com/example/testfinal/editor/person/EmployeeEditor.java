package com.example.testfinal.editor.person;

import com.example.testfinal.model.Employee;
import com.example.testfinal.model.Person;
import com.example.testfinal.model.command.edit.EditPersonCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmployeeEditor implements PersonEditor<Employee> {

    @Override
    public String getType() {
        return "EMPLOYEE";
    }

    @Override
    public Employee edit(Employee employee, EditPersonCommand command) {
        Map<String, Object> parameters = command.getParameters();
        String name = getStringParameters("name", parameters);
        String surname = getStringParameters("surname", parameters);
        long pesel = getLongParameters("pesel", parameters);
        int height = getIntegerParameters("height", parameters);
        int weight = getIntegerParameters("weight", parameters);
        String email = getStringParameters("email", parameters);
        String employmentStartDateString = getStringParameters("employmentStartDate", parameters);
        LocalDate employmentStartDate = LocalDate.parse(employmentStartDateString);
        String positions = getStringParameters("position", parameters);
        int salary = getIntegerParameters("salary", parameters);

        employee.setName(name);
        employee.setSurname(surname);
        employee.setPesel(pesel);
        employee.setHeight(height);
        employee.setWeight(weight);
        employee.setEmail(email);
        employee.setEmploymentStartDate(employmentStartDate);
        employee.setPosition(positions);
        employee.setSalary(salary);

        return employee;
    }
}
