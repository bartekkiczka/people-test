package com.example.testfinal.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class PersonDto {

    private long id;

    private String type;

    private String name;

    private String surname;

    private long pesel;

    private Integer height;

    private Integer weight;

    private String email;
}
