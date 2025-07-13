package com.example.demo.dao.repository;

import com.example.demo.domain.PersonPhone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonPhoneJpaRepository extends JpaRepository<PersonPhone, Long> {
}
