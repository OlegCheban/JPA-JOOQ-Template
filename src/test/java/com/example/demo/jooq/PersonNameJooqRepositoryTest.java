package com.example.demo.jooq;

import com.example.demo.dao.jooq.PersonJooqRepository;
import org.jooq.DSLContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.jooq.codegen.demo.Sequences.PERSON_SEQ;
import static org.jooq.codegen.demo.Tables.PERSON;

@Testcontainers
@DataJpaTest
@ContextConfiguration(classes = {JooqConfig.class})
@ActiveProfiles("jooq")
public class PersonNameJooqRepositoryTest {

    @Autowired
    private PersonJooqRepository personJooqRepository;

    @Autowired
    private DSLContext dslContext;

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

    @BeforeEach
    void setUp() {
        dslContext.insertInto(PERSON)
                .values(PERSON_SEQ.nextval(), "John Doe", 30)
                .values(PERSON_SEQ.nextval(), "Jane Smith", 25)
                .execute();
    }

    @Test
    void shouldSaveAndRetrievePerson(){
        var persons = personJooqRepository.getPersonByName("John Doe");
        Assertions.assertFalse(persons.isEmpty());
        Assertions.assertEquals(1, persons.size());
    }
}
