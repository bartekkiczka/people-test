package com.example.testfinal.model;

import com.example.testfinal.enums.PersonTypes;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "person_type", discriminatorType = DiscriminatorType.STRING)
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "UC_PERSON_EMAIL", columnNames = "email"),
        @UniqueConstraint(name = "UC_PERSON_PESEL", columnNames = "pesel")
})
@Where(clause = "deleted=false")
public abstract class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    @Column(insertable = false, updatable = false, name = "person_type")
    private PersonTypes type;

    private String name;

    private String surname;

    private long pesel;

    private Integer height;

    private Integer weight;

    private String email;

    @Version
    private Long version;

    private boolean deleted = false;
}
