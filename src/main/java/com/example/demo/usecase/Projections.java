package com.example.demo.usecase;

import com.example.demo.dao.projection.BriefPersonInfo;
import com.example.demo.dao.repository.PersonJpaRepository;
import org.springframework.stereotype.Service;

@Service
public class Projections {
    private final PersonJpaRepository personRepository;

    public Projections(PersonJpaRepository personRepository) {
        this.personRepository = personRepository;
    }

    public BriefPersonInfo findBriefPersonInfo(Long personId) {
        return personRepository.findById(personId, BriefPersonInfo.class);
    }
}
