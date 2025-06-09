package com.example.demo.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "person_phone")
public class PersonPhone extends BaseEntity {

    private String phoneNumber;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Person.class)
    private Person person;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public PersonPhone() {
    }
}
