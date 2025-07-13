package com.example.demo.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "person_phone")
public class PersonPhone extends BaseEntity {

    private String phoneNumber;

    //This is the most natural and efficient way of mapping a database one-to-many database association.
    //@ManyToOne annotation on the child side is everything you need
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id")
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

    public PersonPhone(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public PersonPhone(Person person, String phoneNumber) {
        this.person = person;
        this.phoneNumber = phoneNumber;
    }
}
