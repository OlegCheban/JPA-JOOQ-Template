package com.example.demo.dao.repository;

import com.example.demo.domain.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonJpaRepository extends JpaRepository<Person, Long> {
    //use dynamic projection methods for fetching projections when a where clause can be derived from the method name
    <T> T findById(Long id, Class<T> clazz);

    //use it only when you need the complete entity with all its fields. It's inefficient if you only need some fields
    Optional<Person> findByName(String name);
}
