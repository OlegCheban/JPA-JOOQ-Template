package com.example.demo.usecase;

import com.example.demo.domain.Person;
import com.example.demo.service.PersonPhoneService;
import com.example.demo.service.PersonService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

@Service
public class OptimizedTransactionBoundaries {
    private final PersonService personService;
    private final PersonPhoneService personPhoneService;
    private final TransactionTemplate transactionTemplate;

    public OptimizedTransactionBoundaries(PersonService personService, PersonPhoneService personPhoneService, TransactionTemplate transactionTemplate) {
        this.personService = personService;
        this.personPhoneService = personPhoneService;
        this.transactionTemplate = transactionTemplate;
    }

    @Transactional
    public void withNoDatabaseCallBefore() {
        //database connection isn't going to be received here
        //noDatabaseCall();

        //database connection is going to be received here because hikari auto-commit = false
        Person person = personService.addPerson("Oleg");
        personPhoneService.addPhone(person.getId(), "123456789");
    }


    //in this case, we set the transaction scope via TransactionTemplate
    //@Transactional
    public void withNoDatabaseCallAfter() {
        transactionTemplate.executeWithoutResult(transactionStatus -> {
            Person person = personService.addPerson("Oleg");
            personPhoneService.addPhone(person.getId(), "123456789");

        }); //the transaction ends here, and the database connection is released immediately

        noDatabaseCall();
    }

    private void noDatabaseCall() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
