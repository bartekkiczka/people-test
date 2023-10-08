package com.example.testfinal.model.dto;

import com.example.testfinal.enums.PersonTypes;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDto extends PersonDto {

    @Builder
    public EmployeeDto(long id, String name, String surname, long pesel, Integer height, Integer weight,
                       String email, LocalDate employmentStartDate, String position, Integer salary) {
        super(id, PersonTypes.EMPLOYEE, name, surname, pesel, height, weight, email);
        this.employmentStartDate = employmentStartDate;
        this.position = position;
        this.salary = salary;
    }

    private LocalDate employmentStartDate;

    private String position;

    private Integer salary;
}
