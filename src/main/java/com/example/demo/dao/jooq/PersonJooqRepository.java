package com.example.demo.dao.jooq;

import org.jooq.DSLContext;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.jooq.codegen.demo.Sequences.PERSON_SEQ;
import static org.jooq.codegen.demo.Tables.PERSON;

@Component
public class PersonJooqRepository {

    private final DSLContext dsl;

    public PersonJooqRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

    public void insertPerson(String name){
        dsl.insertInto(PERSON).values(PERSON_SEQ.nextval(), name).execute();
    }

    public List<Persons> getPersonByName(String name){
        return dsl.select(PERSON.ID, PERSON.NAME)
                .from(PERSON)
                .where(PERSON.NAME.eq(name))
                .fetchInto(Persons.class);
    }
}
