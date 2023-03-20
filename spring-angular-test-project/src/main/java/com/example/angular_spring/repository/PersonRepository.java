package com.example.angular_spring.repository;

import com.example.angular_spring.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    Optional<Person> findPersonByUsername(String username);
    Optional<Person> findPersonByEmail(String email);
}
