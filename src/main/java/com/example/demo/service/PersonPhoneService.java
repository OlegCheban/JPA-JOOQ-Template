package com.example.demo.service;

import com.example.demo.dao.repository.PersonJpaRepository;
import com.example.demo.dao.repository.PersonPhoneJpaRepository;
import com.example.demo.domain.Person;
import com.example.demo.domain.PersonPhone;
import org.springframework.stereotype.Service;

@Service
public class PersonPhoneService {
    private final PersonJpaRepository personRepository;
    private final PersonPhoneJpaRepository phoneRepository;

    public PersonPhoneService(PersonJpaRepository personRepository, PersonPhoneJpaRepository phoneRepository) {
        this.personRepository = personRepository;
        this.phoneRepository = phoneRepository;
    }

    public PersonPhone addPhone(Long personId, String phoneNumber) {
        Person person = personRepository.getReferenceById(personId);
        return phoneRepository.save(new PersonPhone(person, phoneNumber));
    }
}
