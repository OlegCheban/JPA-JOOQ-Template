package com.example.demo.jooq;

import com.example.demo.dao.jooq.PersonJooqRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Testcontainers
@DataJpaTest
@ContextConfiguration(classes = {JooqTestConfig.class})
public class PersonJooqRepositoryTest {

    @Autowired
    private PersonJooqRepository personJooqRepository;

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

    @Test
    void shouldSaveAndRetrievePerson(){
        personJooqRepository.insertPerson("John Doe");
        var persons = personJooqRepository.getPersonByName("John Doe");
        assertThat(persons.getFirst().name()).isEqualTo("John Doe");
    }
}
