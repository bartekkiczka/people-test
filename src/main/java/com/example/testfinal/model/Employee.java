package com.example.testfinal.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue("EMPLOYEE")
@SQLDelete(sql = "update person set deleted = true where id = ? and version = ?")
public class Employee extends Person {

    @Builder
    public Employee(String name, String surname, long pesel, Integer height, Integer weight,
                    String email, LocalDate employmentStartDate, String position, Integer salary){
        this.setType("EMPLOYEE");
        this.setName(name);
        this.setSurname(surname);
        this.setPesel(pesel);
        this.setHeight(height);
        this.setWeight(weight);
        this.setEmail(email);
        this.employmentStartDate = employmentStartDate;
        this.position = position;
        this.salary = salary;
    }

    @Column(name = "employment_start_date")
    private LocalDate employmentStartDate;

    private String position;

    private Integer salary;

    @OneToMany(mappedBy = "employee")
    private List<Job> jobs = new ArrayList<>();
}
