package com.example.demo.domain;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "person")
public class Person extends BaseEntity {

    private String name;

    private Integer age;

    /**
     * OneToMany collections can cause performance and memory issues when the "many" side grows large,
     * so they should be used judiciously rather than by default.
     *
     * Most of the time, the @ManyToOne annotation on the child side (PersonPhone in our case) is everything you need.
     * @see PersonPhone
     */
    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PersonPhone> personPhones = new ArrayList<>();

    public Person(String name) {
        this.name = name;
    }

    public Person() {
    }

    public void addPhone(PersonPhone phone) {
        personPhones.add(phone);
        phone.setPerson(this);
    }

    public void removePhone(PersonPhone phone) {
        personPhones.remove(phone);
        phone.setPerson(null);
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
