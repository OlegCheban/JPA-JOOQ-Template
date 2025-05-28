package com.example.demo.service;

import com.example.demo.dao.repository.PersonJpaRepository;
import com.example.demo.domain.Person;
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
    public void addPerson(){
        Person person = new Person("John Doe");
        repository.save(person);
    }

    @Transactional
    public void withNoDatabaseCallBefore(){
        noDatabaseCall();
        //transaction starts here because hikari auto-commit = false
        repository.findByName("Test");
    }

    //@Transactional!!!
    public void withNoDatabaseCallAfter(){
        transactionTemplate.executeWithoutResult(transactionStatus -> {
            repository.findByName("Test");
        });
        //transaction stops here
        noDatabaseCall();
    }

    private void noDatabaseCall(){
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
