package com.udacity.jdnd.course3.critter.schedule;

import com.udacity.jdnd.course3.critter.pet.PetEntity;
import com.udacity.jdnd.course3.critter.user.EmployeeEntity;
import com.udacity.jdnd.course3.critter.user.EmployeeSkill;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Entity
public class ScheduleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @ManyToMany()
    private List<EmployeeEntity> employeeEntityList;
    @ManyToMany()
    private List<PetEntity> petEntityList;
    private LocalDate date;
    @ElementCollection
    private Set<EmployeeSkill> employeeSkills;
}