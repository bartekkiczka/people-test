package com.example.testfinal.model.dto;

import com.example.testfinal.enums.PersonTypes;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StudentDto extends PersonDto {

    @Builder
    public StudentDto(long id, String name, String surname, long pesel, Integer height, Integer weight,
                      String email, String schoolName, Integer yearOfStudy, String fieldOfStudy, Integer scholarship) {
        super(id, PersonTypes.STUDENT, name, surname, pesel, height, weight, email);
        this.schoolName = schoolName;
        this.yearOfStudy = yearOfStudy;
        this.fieldOfStudy = fieldOfStudy;
        this.scholarship = scholarship;
    }

    private String schoolName;

    private Integer yearOfStudy;

    private String fieldOfStudy;

    private Integer scholarship;
}
