package com.udacity.jdnd.course3.critter.pet;

import com.udacity.jdnd.course3.critter.user.CustomerEntity;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class PetEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private PetType type;
    private String name;
    @ManyToOne
    private CustomerEntity customerEntity;
    private LocalDate birthDate;
    private String notes;
}
