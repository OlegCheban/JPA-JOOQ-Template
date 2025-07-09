[![Build](https://github.com/OlegCheban/JPA-JOOQ-Template/actions/workflows/mvn.yml/badge.svg)](https://github.com/OlegCheban/JPA-JOOQ-Template/actions/workflows/mvn.yml)

# JPA-JOOQ-Template
A pre-configured, performance-oriented Spring Boot template and cookbook for database operations, combining JPA (Hibernate) for ORM and JOOQ for type-safe SQL. The template is optimized for enterprise-grade, high-performance database operations rather than simple CRUD services. The configurations have been implemented to maximize performance and resource efficiency.

**Tech Stack**: Spring Boot, Maven, Hibernate, JOOQ, Liquibase, PostgreSQL, HikariCP, JUnit, Testcontainers.   

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

## 📚 Cookbook

This section contains practical examples and best practices for optimizing database operations in Spring Boot applications.

### 1. Avoid long-running transactions

**Problem:** Using `@Transactional` on methods that perform non-database operations after database calls keeps database connections open unnecessarily, leading to connection pool exhaustion and poor performance.

#### ❌ DON'T DO THIS

```java
@Service
public class PersonService {
    private final PersonJpaRepository repository;
    
    public PersonService(PersonJpaRepository repository) {
        this.repository = repository;        
    }
    
    @Transactional
    public void withNoDatabaseCallAfter() {
        repository.save(new Person("Test"));
        noDatabaseCall(); // ⚠️ Transaction remains open during this call
    }
    
    private void noDatabaseCall() {
        try {
            Thread.sleep(2000); // Simulates non-DB work (e.g., external API calls, file processing)
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
```

**Issues:**
- Database connection held for 2+ seconds unnecessarily
- Connection pool can be exhausted under load
- Poor resource utilization

#### ✅ INSTEAD, DO THIS

```java
@Service
public class PersonService {
    private final PersonJpaRepository repository;
    private final TransactionTemplate transactionTemplate;
    
    public PersonService(PersonJpaRepository repository, TransactionTemplate transactionTemplate) {
        this.repository = repository;
        this.transactionTemplate = transactionTemplate;
    }
    
    public void withNoDatabaseCallAfter() {
        transactionTemplate.executeWithoutResult(transactionStatus -> {
            repository.save(new Person("Test"));
        }); // ✅ Transaction closes here
        
        noDatabaseCall(); // Non-DB work happens outside transaction
    }
    
    private void noDatabaseCall() {
        try {
            Thread.sleep(2000); // Now runs without holding DB connection
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
```

**Benefits:**
- Database connection released immediately after `save()` operation
- Better connection pool utilization
- Improved application scalability
- Clear separation of transactional and non-transactional code

**Key Principle:** Keep transactions as short as possible. Only database operations should run within transactional boundaries.


### 2. Use `getReferenceById()` when you only need a reference

**Problem:** Fetching a full entity (findById) when you only need a reference (e.g., for setting relationships) leads to unnecessary database queries and increased memory usage.

#### ❌ DON'T DO THIS

```java
public Order createOrderForCustomer(Long customerId, BigDecimal amount) {
    Customer customer = customerRepository.findById(customerId);
    Order newOrder = new Order();
    newOrder.setCustomer(customer);
    newOrder.setTotalAmount(amount);
    return orderRepository.save(newOrder);
}
```

**Issues:**
- Performs a database query to load the entire Customer entity
- Slower under high-load scenarios or with large associated entities

#### ✅ INSTEAD, DO THIS

```java
public Order createOrderForCustomer(Long customerId, BigDecimal amount) {
    Customer customer = customerRepository.getReferenceById(customerId);
    Order newOrder = new Order();
    newOrder.setCustomer(customer);
    newOrder.setTotalAmount(amount);
    return orderRepository.save(newOrder);
}
```

**Benefits:**
- No unnecessary SQL SELECT query is executed
- Better overall performance, especially in write-heavy or large-domain models

**Key Principle:** Use `getReferenceById()` when you only need the entity reference, not its data.


---

> 💡 **Tip**: More cookbook examples will be added as the template evolves. Each example focuses on real-world performance optimization scenarios.