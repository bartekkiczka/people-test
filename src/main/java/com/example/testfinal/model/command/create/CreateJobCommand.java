package com.example.testfinal.model.command.create;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class CreateJobCommand {

    @NotNull(message = "NAME_IS_NULL")
    @NotEmpty(message = "JOB_NAME_IS_EMPTY")
    private String name;

    @NotNull(message = "SALARY_IS_NULL")
    @Positive(message = "SALARY_NOT_POSITIVE")
    private Long salary;

    @NotNull(message = "START_DATE_IS_NULL")
    @PastOrPresent(message = "START_DATE_NOT_IN_PAST")
    private LocalDate startDate;

    @PastOrPresent(message = "END_DATE_NOT_IN_PAST")
    @NotNull(message = "END_DATE_IS_NULL")
    private LocalDate endDate;
}
