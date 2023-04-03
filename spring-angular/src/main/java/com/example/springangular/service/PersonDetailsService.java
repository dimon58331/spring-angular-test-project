package com.example.springangular.service;

import com.example.angular_spring.entity.Person;
import com.example.angular_spring.repository.PersonRepository;
import com.example.angular_spring.security.PersonDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PersonDetailsService implements UserDetailsService {
    private final PersonRepository personRepository;

    @Autowired
    public PersonDetailsService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Person> person = personRepository.findPersonByUsername(username);
        if (person.isEmpty()){
            throw new UsernameNotFoundException("User with this username not found");
        }
        return new PersonDetails(person.get());
    }
}
