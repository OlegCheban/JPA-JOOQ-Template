package com.example.demo.jpa;

import com.example.demo.dao.repository.PersonJpaRepository;
import com.example.demo.domain.Person;
import com.example.demo.service.PersonService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Testcontainers
@DataJpaTest
@ContextConfiguration(classes = {JpaConfig.class})
public class PersonNameJpaRepositoryTest {

    @Autowired
    private PersonJpaRepository personJpaRepository;

    @Autowired
    private PersonService personService;

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

    @Test
    void connectionEstablished(){
        assertThat(postgres.isCreated()).isTrue();
        assertThat(postgres.isRunning()).isTrue();
    }

    @Test
    @Transactional
    void shouldSaveAndRetrievePerson() {
        final String name = "John Doe";
        personService.addPerson(name);
        Optional<Person> foundPerson = personJpaRepository.findByName(name);

        assertThat(foundPerson).isPresent();
        assertThat(foundPerson.get().getName()).isEqualTo(name);
    }
}
