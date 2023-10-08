package com.example.testfinal.model.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JobDto {
    private long id;
    private String name;
    private Long salary;
    private LocalDate startDate;
    private LocalDate endDate;
    private long employeeId;
}
