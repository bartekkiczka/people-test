package com.example.testfinal.model.command.create;

import com.example.testfinal.enums.PersonTypes;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class CreateEmployeeCommand {

    @Enumerated(EnumType.STRING)
    @NotNull(message = "TYPE_IS_NULL")
    private PersonTypes type;

    @NotNull(message = "NAME_IS_NULL")
    @NotEmpty(message = "NAME_IS_EMPTY")
    @Length(min = 3, message = "NAME_TOO_SHORT")
    private String name;

    @NotNull(message = "SURNAME_IS_NULL")
    @NotEmpty(message = "SURNAME_IS_EMPTY")
    @Length(min = 3, message = "SURNAME_TOO_SHORT")
    private String surname;

    @Min(value = 10000000000L, message = "PESEL_IS_LESS_THAN_11_DIGITS")
    @Max(value = 99999999999L, message = "PESEL_IS_MORE_THAN_11_DIGITS")
    @NotNull(message = "PESEL_IS_NULL")
    private Long pesel;

    @NotNull(message = "HEIGHT_IS_NULL")
    @Positive(message = "HEIGHT_IS_NEGATIVE")
    private Integer height;

    @NotNull(message = "WEIGHT_IS_NULL")
    @Positive(message = "WEIGHT_IS_NEGATIVE")
    private Integer weight;

    @NotNull(message = "EMAIL_IS_NULL")
    @Email(message = "EMAIL_WRONG_FORMAT")
    private String email;

    @PastOrPresent(message = "EMPLOYMENT_START_DATE_IS_IN_THE_FUTURE")
    @NotNull(message = "EMPLOYMENT_START_DATE_IS_NULL")
    private LocalDate employmentStartDate;

    @NotNull(message = "POSITION_IS_NULL")
    @NotEmpty(message = "POSITION_IS_EMPTY")
    private String position;

    @NotNull(message = "SALARY_IS_NULL")
    @Positive(message = "SALARY_IS_NEGATIVE_OR_ZERO")
    private Integer salary;
}
