package com.example.demo.domain;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "person")
public class Person extends BaseEntity {

    private String name;

    private Integer age;

    @OneToMany(fetch = FetchType.LAZY, targetEntity = PersonPhone.class)
    @JoinColumn(name = "person_id")
    private Set<PersonPhone> personPhones;

    public Person(String name) {
        this.name = name;
    }

    public Person() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
