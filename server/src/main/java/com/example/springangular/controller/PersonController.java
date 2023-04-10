package com.example.springangular.controller;

import com.example.springangular.dto.PersonDTO;
import com.example.springangular.entity.Person;
import com.example.springangular.service.PersonService;
import com.example.springangular.validator.ResponseErrorValidation;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Objects;

@RestController
@CrossOrigin
@RequestMapping("/api/person")
public class PersonController {
    private static final Logger LOG = LoggerFactory.getLogger(PersonController.class);
    private final PersonService personService;
    private final ResponseErrorValidation responseErrorValidation;
    private final ModelMapper modelMapper;

    @Autowired
    public PersonController(PersonService personService, ResponseErrorValidation responseErrorValidation, ModelMapper modelMapper) {
        this.personService = personService;
        this.responseErrorValidation = responseErrorValidation;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/")
    public ResponseEntity<PersonDTO> getCurrentPerson(Principal principal) {
        Person person = personService.getCurrentPerson(principal);
        PersonDTO personDTO = convertPersonToPersonDTO(person);
        LOG.info("Convert is successful");
        LOG.info(personDTO.toString());

        return new ResponseEntity<>(personDTO, HttpStatus.OK);
    }

    @GetMapping("/{personId}")
    public ResponseEntity<PersonDTO> getPersonProfile(@PathVariable("personId") String personId){
        Person person = personService.findPersonById(Long.parseLong(personId));
        PersonDTO personDTO = convertPersonToPersonDTO(person);

        return new ResponseEntity<>(personDTO, HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<Object> updatePerson(@Valid @RequestBody PersonDTO personDTO, BindingResult bindingResult
            , Principal principal){
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (Objects.nonNull(errors)) return errors;

        Person person = personService.updatePerson(personDTO, principal);
        PersonDTO updatedPersonDTO = convertPersonToPersonDTO(person);

        return new ResponseEntity<>(updatedPersonDTO, HttpStatus.OK);
    }

    public PersonDTO convertPersonToPersonDTO(Person person){
        return modelMapper.map(person, PersonDTO.class);
    }
}
