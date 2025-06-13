# JPA-JOOQ-Template
A pre-configured Spring Boot template for database operations, combining JPA (Hibernate) for ORM and JOOQ for type-safe SQL. The template is optimized for enterprise-grade, high-performance database operations rather than simple CRUD services. The configurations have been implemented to maximize performance and resource efficiency.

## Key Features

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
- **Transaction control**: With Spring's `@Transactional` annotations managing transaction boundaries, auto-commit should be disabled to prevent conflicts between Spring's transaction management and connection-level auto-commit behavior.
- **Performance optimization**: Reduces overhead by eliminating unnecessary commit operations.
- **Consistency**: Ensures all database operations within a transaction are committed together, maintaining ACID properties.
- **Best practice alignment**: Industry standard for JPA/Hibernate applications where framework-managed transactions are preferred over connection-level transaction management.


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
