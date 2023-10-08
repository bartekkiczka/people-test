package com.example.testfinal.model.dto;

import com.example.testfinal.enums.PersonTypes;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PensionerDto extends PersonDto {

    @Builder
    public PensionerDto(long id, String name, String surname, long pesel, Integer height, Integer weight,
                        String email, Integer pension, Integer yearsWorked) {
        super(id, PersonTypes.PENSIONER, name, surname, pesel, height, weight, email);
        this.pension = pension;
        this.yearsWorked = yearsWorked;
    }

    private Integer pension;

    private Integer yearsWorked;
}
