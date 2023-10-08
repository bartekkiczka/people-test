package com.example.testfinal.mappings.dto;

import com.example.testfinal.model.Employee;
import com.example.testfinal.model.dto.EmployeeDto;
import com.example.testfinal.model.dto.PersonDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeToPersonDtoConverter implements Converter<Employee, PersonDto> {

    @Override
    public PersonDto convert(MappingContext<Employee, PersonDto> mappingContext) {
        Employee employee = mappingContext.getSource();

        return EmployeeDto.builder()
                .id(employee.getId())
                .name(employee.getName())
                .surname(employee.getSurname())
                .pesel(employee.getPesel())
                .height(employee.getHeight())
                .weight(employee.getWeight())
                .email(employee.getEmail())
                .employmentStartDate(employee.getEmploymentStartDate())
                .position(employee.getPosition())
                .salary(employee.getSalary())
                .build();
    }
}
