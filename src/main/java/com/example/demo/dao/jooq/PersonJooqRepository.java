package com.example.demo.dao.jooq;

import com.example.demo.dao.projection.BriefPersonInfo;
import org.jooq.DSLContext;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.jooq.codegen.demo.Tables.PERSON;

@Component
public class PersonJooqRepository {

    private final DSLContext dsl;

    public PersonJooqRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

    public List<BriefPersonInfo> getPersonByName(String name){
        return dsl.select(PERSON.ID, PERSON.NAME, PERSON.AGE)
                .from(PERSON)
                .where(PERSON.NAME.eq(name))
                .fetchInto(BriefPersonInfo.class);
    }
}
