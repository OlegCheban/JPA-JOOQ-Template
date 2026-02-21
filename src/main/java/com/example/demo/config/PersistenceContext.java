package com.example.demo.config;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DataSourceConnectionProvider;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultDSLContext;
import org.jooq.impl.DefaultExecuteListenerProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;

import javax.sql.DataSource;

@Configuration
public class PersistenceContext {

    @Bean
    public DataSourceConnectionProvider connectionProvider(DataSource dataSource) {
        return new DataSourceConnectionProvider(new TransactionAwareDataSourceProxy(dataSource));
    }

    @Bean
    public SQLExceptionTranslator sqlExceptionTranslator(DataSource dataSource) {
        return new SQLErrorCodeSQLExceptionTranslator(dataSource);
    }

    @Bean
    public ExceptionTranslator exceptionTranslator(SQLExceptionTranslator translator) {
        return new ExceptionTranslator(translator);
    }

    @Bean
    public DefaultConfiguration configuration(
            DataSourceConnectionProvider connectionProvider,
            ExceptionTranslator exceptionTranslator) {
        DefaultConfiguration JooqConfiguration = new DefaultConfiguration();
        JooqConfiguration.set(connectionProvider);
        JooqConfiguration.set(SQLDialect.POSTGRES);
        JooqConfiguration.set(new DefaultExecuteListenerProvider(exceptionTranslator));
        return JooqConfiguration;
    }

    @Bean
    public DSLContext dslContext(org.jooq.Configuration jooqConfiguration) {
        return new DefaultDSLContext(jooqConfiguration);
    }
}
