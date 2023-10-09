package com.example.testfinal.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;
import org.hibernate.annotations.SQLDelete;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("STUDENT")
@SQLDelete(sql = "update person set deleted = true where id = ? and version = ?")
public class Student extends Person {

    @Builder
    public Student(String name, String surname, long pesel, Integer height, Integer weight,
                   String email, String schoolName, Integer yearOfStudy,
                   String fieldOfStudy, Integer scholarship) {
        this.setType("STUDENT");
        this.setName(name);
        this.setSurname(surname);
        this.setPesel(pesel);
        this.setHeight(height);
        this.setWeight(weight);
        this.setEmail(email);
        this.schoolName = schoolName;
        this.yearOfStudy = yearOfStudy;
        this.fieldOfStudy = fieldOfStudy;
        this.scholarship = scholarship;
    }

    @Column(name = "school_name")
    private String schoolName;

    @Column(name = "year_of_study")
    private Integer yearOfStudy;

    @Column(name = "field_of_study")
    private String fieldOfStudy;

    private Integer scholarship;
}
