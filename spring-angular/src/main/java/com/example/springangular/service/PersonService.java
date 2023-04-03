package com.example.springangular.service;

import com.example.angular_spring.entity.Person;
import com.example.angular_spring.exception.UserExistsException;
import com.example.angular_spring.repository.PersonRepository;
import com.example.angular_spring.util.ERole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;

@Service
@Transactional(readOnly = true)
public class PersonService {
    private final PasswordEncoder passwordEncoder;
    private final PersonRepository personRepository;
    private final Logger LOG = LoggerFactory.getLogger(PersonService.class);
    @Autowired
    public PersonService(PasswordEncoder passwordEncoder, PersonRepository personRepository) {
        this.passwordEncoder = passwordEncoder;
        this.personRepository = personRepository;
    }

    @Transactional
    public void createUser(Person person){
        person.getRoles().add(ERole.ROLE_USER);
        person.setPassword(passwordEncoder.encode(person.getPassword()));

        try {
            LOG.atError().log("Bad save");
            personRepository.save(person);
        } catch(Exception e){
            LOG.atError().log("Throwing exception");
            throw new UserExistsException("The user " + person.getUsername() + " already exists.");
        }
    }

    @Transactional
    public void updatePerson(Person person){
        personRepository.save(person);
    }

    public Person getCurrentPerson(Principal principal){
        return getPersonByPrincipal(principal);
    }

    private Person getPersonByPrincipal(Principal principal){
        String username = principal.getName();
        return personRepository.findPersonByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username '" + username + "' not found"));
    }

}
