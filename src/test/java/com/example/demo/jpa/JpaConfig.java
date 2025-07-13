package com.example.demo.jpa;

import com.example.demo.dao.repository.PersonJpaRepository;
import com.example.demo.service.PersonService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class JpaConfig {
    @Bean
    public PersonService users(PersonJpaRepository r) {
        return new PersonService(r);
    }
}
