package com.example.demo.jooq;

import com.example.demo.dao.jooq.PersonJooqRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@DataJpaTest
@ContextConfiguration(classes = {JooqConfig.class})
public class PersonJooqRepositoryTest {

    @Autowired
    private PersonJooqRepository personJooqRepository;

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

    @Test
    void shouldSaveAndRetrievePerson(){
        var persons = personJooqRepository.getPersonByName("John Doe");

        Assertions.assertTrue(persons.isEmpty());
    }
}
