[![Build](https://github.com/OlegCheban/JPA-JOOQ-Template/actions/workflows/mvn.yml/badge.svg)](https://github.com/OlegCheban/JPA-JOOQ-Template/actions/workflows/mvn.yml)

# JPA-JOOQ-Template

A pre-configured Spring Boot template for database operations, combining JPA (Hibernate) for ORM and JOOQ for type-safe SQL. The configuration has been implemented to maximize performance and resource efficiency.

**Tech Stack:** Spring Boot, Maven, Hibernate, JOOQ, Liquibase, PostgreSQL, HikariCP, JUnit, Testcontainers.

**Important:** This template includes Hibernate/JPA, which adds complexity and overhead. Use it only if you intentionally want an ORM and benefit from its features. You probably want this template if you have rich domain models with non-trivial business logic and invariants, lots of complex writing operations, or you need specific Hibernate features.

## Configuration Guidelines

### Core Framework Settings

#### Open Session in View (OSIV) - Disabled ✅
```yaml
spring:
  jpa:
    open-in-view: false
```
**Why disabled:**
- **Performance:** OSIV keeps database connections open throughout the entire HTTP request lifecycle, including view rendering. This leads to longer connection hold times and potential connection pool exhaustion under load.
- **Resource efficiency:** Disabling OSIV ensures database connections are released immediately after transactional operations complete, freeing up resources for other operations.

#### HikariCP Auto-Commit - Disabled ✅
```yaml
spring:
  datasource:
    hikari:
      auto-commit: false
```
**Why disabled:**
- **Connection management efficiency:** A positive side effect of this setting is improved database connection management. Spring will not open a database connection until it reaches the point where it actually needs to access the database. This reduces idle connections and improves resource utilization.
- **Best practice alignment:** A good rule of thumb is to always disable auto-commit in HikariCP (or other connection pools) when using local JPA transactions.

### JPA/Hibernate Performance Best Practices

#### Transaction Management
- **Call external services OUTSIDE of database transactions** to minimize transaction duration
- **Be very careful with `@Transactional(propagation = REQUIRES_NEW)`** - it creates separate transactions that can lead to deadlocks and performance issues
- **Use `TransactionTemplate`** when you need fine-grained control over transaction boundaries

```java
// ❌ Avoid - External service call inside transaction
@Transactional
public void processOrder(Order order) {
    orderRepository.save(order);
    emailService.sendConfirmation(order); // This keeps transaction open!
}

// ✅ Better - External service outside transaction
public void processOrder(Order order) {
    Order savedOrder = transactionTemplate.execute(status -> {
        return orderRepository.save(order);
    });
    emailService.sendConfirmation(savedOrder); // Outside transaction
}
```

#### Entity Versioning and Optimistic Locking
- **Avoid `select` on `insert` with `@Version`** or implement `Persistable<ID>` interface to prevent unnecessary SELECT queries before INSERT operations

```java
// ✅ Implement Persistable to avoid select-before-insert
@Entity
public class OptimizedEntity implements Persistable<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Version
    private Long version;
    
    @Override
    public boolean isNew() {
        return id == null; // Prevents Hibernate from doing SELECT before INSERT
    }
}
```

#### Repository and Query Optimization
- **Use `Repository#getReferenceById`** when you only need entity references (avoids unnecessary database hits)

```java
// ❌ Avoid - Loads full entity from database
User user = userRepository.findById(userId).orElseThrow();
order.setUser(user);

// ✅ Better - Gets reference without database hit
User userRef = userRepository.getReferenceById(userId);
order.setUser(userRef);
```

#### Fetch Strategy Optimization
- **Always use `FetchType.LAZY` on `@OneToMany` and `@ManyToMany` relationships** to prevent N+1 queries and unnecessary data loading
- **Explicitly fetch associations** using `fetch join` in JPQL or `@EntityGraph` when you actually need the related data

```java
// ✅ Always lazy for collections
@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
private List<Order> orders;

// ✅ Explicit fetching when needed
@Query("SELECT u FROM User u JOIN FETCH u.orders WHERE u.id = :id")
User findUserWithOrders(@Param("id") Long id);

// ✅ Or use @EntityGraph
@EntityGraph(attributePaths = {"orders"})
Optional<User> findWithOrdersById(Long id);
```

#### Dynamic Projection Methods
- **Use dynamic projection methods** for fetching projections when a WHERE clause can be derived from the method name

```java
// ✅ Dynamic projection - Spring Data JPA handles projection automatically
public interface UserRepository extends JpaRepository<User, Long> {
    <T> T findById(Long id, Class<T> type);
    <T> List<T> findByActiveTrue(Class<T> type);
    <T> Optional<T> findByEmail(String email, Class<T> type);
}

// Usage examples
UserSummary summary = userRepository.findById(1L, UserSummary.class);
UserDto dto = userRepository.findById(1L, UserDto.class);
List<UserSummary> activeSummaries = userRepository.findByActiveTrue(UserSummary.class);

// Projection record
public record UserSummary(Long id, String name, String email) {}
```

#### Bulk Operations
- **Use `@DynamicUpdate`** for entities with large numbers of columns to generate UPDATE statements with only changed fields

```java
@Entity
@DynamicUpdate // Only updates changed columns
public class LargeEntity {
    // Entity with many columns
}
```

### Database Migration Configuration

#### Liquibase Session-Level LockService ✅
```xml
<!-- https://mvnrepository.com/artifact/com.github.blagerweij/liquibase-sessionlock -->
<dependency>
  <groupId>com.github.blagerweij</groupId>
  <artifactId>liquibase-sessionlock</artifactId>
</dependency>
```
**Why session-level:**
- **Automatic lock cleanup:** Session-level locks are automatically released when the database connection drops, eliminating the common issue of stuck locks in the DATABASECHANGELOGLOCK table.
- **Improved reliability:** Prevents deployment failures caused by previous migration processes that terminated unexpectedly without releasing their locks.

## Performance Monitoring Tips

### Key Metrics to Track
- **Connection pool utilization** (HikariCP metrics)
- **Transaction duration** (especially long-running transactions)
- **Query execution times** and frequency
- **N+1 query detection** (enable Hibernate statistics in development)

### Development Configuration
```yaml
# Enable for development/testing only
spring:
  jpa:
    show-sql: true
    properties:
      hibernate:
        format_sql: true
        generate_statistics: true
        session:
          events:
            log:
              LOG_QUERIES_SLOWER_THAN_MS: 100
```

## Additional Resources

- [About Pool Sizing](https://github.com/brettwooldridge/HikariCP/wiki/About-Pool-Sizing)
- [About Pool Sizing in distributed environments](https://github.com/brettwooldridge/HikariCP/issues/1023)
- [HikariCP Configuration](https://github.com/brettwooldridge/HikariCP#gear-configuration-knobs-baby)