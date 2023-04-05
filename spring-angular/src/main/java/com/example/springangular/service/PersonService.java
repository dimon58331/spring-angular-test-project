package com.example.springangular.service;

import com.example.springangular.dto.PersonDTO;
import com.example.springangular.entity.Person;
import com.example.springangular.exception.PersonExistsException;
import com.example.springangular.exception.PersonNotFoundException;
import com.example.springangular.repository.PersonRepository;
import com.example.springangular.util.ERole;
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
            throw new PersonExistsException("The user " + person.getUsername() + " already exists.");
        }
    }

    @Transactional
    public Person updatePerson(PersonDTO personDTO, Principal principal){
        Person person = getPersonByPrincipal(principal);
        person.setName(personDTO.getName());
        person.setSurname(personDTO.getSurname());
        person.setEmail(personDTO.getEmail());
        person.setBio(personDTO.getBio());
        person.setUsername(personDTO.getUsername());

        LOG.info(person.toString());

        return person;
    }

    public Person getCurrentPerson(Principal principal){
        return getPersonByPrincipal(principal);
    }

    private Person getPersonByPrincipal(Principal principal){
        String username = principal.getName();
        return personRepository.findPersonByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username '" + username + "' not found"));
    }

    public Person findPersonById(Long personId) {
        return personRepository.findById(personId).orElseThrow(()-> new PersonNotFoundException("Person not be found"));
    }
}
