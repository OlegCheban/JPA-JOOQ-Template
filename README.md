[![Build](https://github.com/OlegCheban/JPA-JOOQ-Template/actions/workflows/mvn.yml/badge.svg)](https://github.com/OlegCheban/JPA-JOOQ-Template/actions/workflows/mvn.yml)

# JPA-JOOQ-Template
A pre-configured, performance-oriented Spring Boot template and cookbook for database operations, combining JPA (Hibernate) for ORM and JOOQ for type-safe SQL. The template is optimized for enterprise-grade, high-performance database operations rather than simple CRUD services. The configurations have been implemented to maximize performance and resource efficiency.

**Tech Stack**: Spring Boot, Maven, Hibernate, JOOQ, Liquibase, PostgreSQL, HikariCP, JUnit, Testcontainers.   

## Recommended Settings

### Open Session in View (OSIV) - Disabled

```yaml
spring:
  jpa:
    open-in-view: false
```

**Why disabled:**
- **Performance**: OSIV keeps database connections open throughout the entire HTTP request lifecycle, including view rendering. This leads to longer connection hold times and potential connection pool exhaustion under load.
- **Resource efficiency**: Disabling OSIV ensures database connections are released immediately after transactional operations complete, freeing up resources for other operations.

### HikariCP Auto-Commit - Disabled

```yaml
spring:
  datasource:
    hikari:
      auto-commit: false
```

**Why disabled:**
- **Connection management efficiency**: A positive side effect of this setting is improved database connection management. Spring will not open a database connection until it reaches the point where it actually needs to access the database. This reduces idle connections and improves resource utilization. 
- **Best practice alignment**: A good rule of thumb is to always disable auto-commit in HikariCP (or other connection pools) when using local JPA transactions.

### Liquibase Session-Level LockService

```xml
<!-- https://mvnrepository.com/artifact/com.github.blagerweij/liquibase-sessionlock -->
<dependency>
  <groupId>com.github.blagerweij</groupId>
  <artifactId>liquibase-sessionlock</artifactId>
</dependency>
```

**Why session-level:**
- **Automatic lock cleanup**: Session-level locks are automatically released when the database connection drops, eliminating the common issue of stuck locks in the `DATABASECHANGELOGLOCK` table.
- **Improved reliability**: Prevents deployment failures caused by previous migration processes that terminated unexpectedly without releasing their locks.

## Links

- [About Pool Sizing](https://github.com/brettwooldridge/HikariCP/wiki/About-Pool-Sizing)
- [About Pool Sizing in distributed environments](https://github.com/brettwooldridge/HikariCP/issues/1023)
- [HikariCP Configuration](https://github.com/brettwooldridge/HikariCP#gear-configuration-knobs-baby)