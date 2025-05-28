package com.example.demo.jpa;

import com.example.demo.dao.repository.PersonJpaRepository;
import com.example.demo.service.PersonService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.support.TransactionTemplate;

@TestConfiguration
public class JpaConfig {

    @Bean
    public PersonService users(PersonJpaRepository r, TransactionTemplate tm) {
        return new PersonService(r, tm);
    }
}
