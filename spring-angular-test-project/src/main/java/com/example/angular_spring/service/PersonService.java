package com.example.angular_spring.service;

import com.example.angular_spring.entity.Person;
import com.example.angular_spring.exception.UserExistsException;
import com.example.angular_spring.repository.PersonRepository;
import com.example.angular_spring.util.ERole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PersonService {
    private final PasswordEncoder passwordEncoder;
    private final PersonRepository personRepository;
    @Autowired
    public PersonService(PasswordEncoder passwordEncoder, PersonRepository personRepository) {
        this.passwordEncoder = passwordEncoder;
        this.personRepository = personRepository;
    }

    @Transactional
    public Person createUser(Person person){
        person.getRoles().add(ERole.ROLE_USER);
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        try {
            return personRepository.save(person);
        } catch(Exception e){
            throw new UserExistsException("The user " + person.getUsername() + " already exists.");
        }

    }

}
