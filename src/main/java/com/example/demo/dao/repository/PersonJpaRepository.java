package com.example.demo.dao.repository;

import com.example.demo.domain.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonJpaRepository extends JpaRepository<Person, Long> {
    Optional<Person> findByName(String name);
}
