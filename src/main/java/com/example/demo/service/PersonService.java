package com.example.demo.service;

import com.example.demo.dao.repository.PersonJpaRepository;
import com.example.demo.domain.Person;
import org.springframework.stereotype.Service;

@Service
public class PersonService {
    private final PersonJpaRepository personRepository;

    public PersonService(PersonJpaRepository personRepository) {
        this.personRepository = personRepository;
    }

    public Person addPerson(String name) {
        Person person = new Person(name);
        return personRepository.save(person);
    }
}