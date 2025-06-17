package com.example.demo.service;

import com.example.demo.dao.projection.PersonName;
import com.example.demo.dao.repository.PersonJpaRepository;
import com.example.demo.domain.Person;
import com.example.demo.domain.PersonPhone;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

@Service
public class PersonService {
    private final PersonJpaRepository repository;
    private final TransactionTemplate transactionTemplate;

    public PersonService(PersonJpaRepository repository, TransactionTemplate transactionTemplate) {
        this.repository = repository;
        this.transactionTemplate = transactionTemplate;
    }

    @Transactional
    public void addPerson(String name) {
        Person person = new Person(name);
        person.addPhone(new PersonPhone("12345"));
        person.addPhone(new PersonPhone("23456"));
        repository.save(person);
    }

    public PersonName findByName(String name) {
        return repository.findByName(name, PersonName.class);
    }

    @Transactional
    public void withNoDatabaseCallBefore() {
        //database connection isn't going to be received here
        noDatabaseCall();

        //database connection is going to be received here because hikari auto-commit = false
        repository.save(new Person("Test"));
    }


    //in this case, we set the transaction scope via TransactionTemplate
    //@Transactional
    public void withNoDatabaseCallAfter() {
        transactionTemplate.executeWithoutResult(transactionStatus -> {
            repository.save(new Person("Test"));
        }); //the transaction ends here, and the database connection is released immediately

        noDatabaseCall();
    }

    private void noDatabaseCall() {
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
