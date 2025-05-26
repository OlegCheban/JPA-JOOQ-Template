package com.example.demo.jooq;

import com.example.demo.dao.jooq.PersonJooqRepository;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

@TestConfiguration
public class JooqTestConfig {

    @Bean
    public DSLContext dslContext(DataSource dataSource) {
        return DSL.using(dataSource, SQLDialect.POSTGRES);
    }

    @Bean
    public PersonJooqRepository users(DSLContext dslContext) {
        return new PersonJooqRepository(dslContext);
    }
}
