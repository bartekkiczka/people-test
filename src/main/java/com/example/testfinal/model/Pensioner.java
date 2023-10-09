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
@DiscriminatorValue("PENSIONER")
@SQLDelete(sql = "update person set deleted = true where id = ? and version = ?")
public class Pensioner extends Person {

    @Builder
    public Pensioner(String name, String surname, long pesel, Integer height, Integer weight,
                     String email, Integer pension, Integer yearsWorked) {
        this.setType("PENSIONER");
        this.setName(name);
        this.setSurname(surname);
        this.setPesel(pesel);
        this.setHeight(height);
        this.setWeight(weight);
        this.setEmail(email);
        this.pension = pension;
        this.yearsWorked = yearsWorked;
    }

    private Integer pension;
    @Column(name = "years_worked")
    private Integer yearsWorked;
}
