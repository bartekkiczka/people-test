package com.example.testfinal.model.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class EmployeeWithJobsDto {
    private long id;

    private String name;

    private String surname;

    private long pesel;

    private Integer height;

    private Integer weight;

    private String email;

    private LocalDate employmentStartDate;

    private String position;

    private Integer salary;

    private List<JobDto> jobs;
}
