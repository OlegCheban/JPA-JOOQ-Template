package com.example.demo.config;

import org.jooq.ExecuteContext;
import org.jooq.ExecuteListener;
import org.springframework.jdbc.support.SQLExceptionTranslator;

public class ExceptionTranslator implements ExecuteListener {

    private final SQLExceptionTranslator translator;

    public ExceptionTranslator(SQLExceptionTranslator translator) {
        this.translator = translator;
    }

    public void exception(ExecuteContext context) {
        if (context.sqlException() == null) {
            return;
        }

        var translated = translator.translate(
                "Access database using jOOQ",
                context.sql(),
                context.sqlException()
        );

        if (translated != null) {
            context.exception(translated);
        }
    }
}
