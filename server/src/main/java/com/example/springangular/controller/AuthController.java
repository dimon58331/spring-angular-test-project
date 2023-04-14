package com.example.springangular.controller;

import com.example.springangular.entity.Person;
import com.example.springangular.payload.request.AuthenticationRequest;
import com.example.springangular.payload.request.RegistrationRequest;
import com.example.springangular.payload.response.JWTTokenSuccessResponse;
import com.example.springangular.payload.response.MessageResponse;
import com.example.springangular.service.PersonService;
import com.example.springangular.util.JWTUtil;
import com.example.springangular.validator.ResponseErrorValidation;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@CrossOrigin
@RestController
@RequestMapping("/api/auth")
@PreAuthorize("permitAll()")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final ResponseErrorValidation responseErrorValidation;
    private final JWTUtil jwtUtil;
    private final ModelMapper modelMapper;
    private final PersonService personService;
    private final Logger LOG = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, ResponseErrorValidation responseErrorValidation, JWTUtil jwtUtil, ModelMapper modelMapper, PersonService personService) {
        this.authenticationManager = authenticationManager;
        this.responseErrorValidation = responseErrorValidation;
        this.jwtUtil = jwtUtil;
        this.modelMapper = modelMapper;
        this.personService = personService;
    }

    @PostMapping("/signin")
    public ResponseEntity<Object> authenticatePerson(@Valid @RequestBody AuthenticationRequest authenticationRequest,
                                                     BindingResult result){
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(result);
        if (Objects.nonNull(errors)) return errors;

        LOG.info("Before authenticate");
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authenticationRequest.getUsername(), authenticationRequest.getPassword()
        ));
        LOG.info("After authenticate");

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwtToken = jwtUtil.generateToken(authenticationRequest.getUsername());

        return ResponseEntity.ok(new JWTTokenSuccessResponse(true, jwtToken));
    }

    @PostMapping("/signup")
    public ResponseEntity<Object> registerPerson(@Valid @RequestBody RegistrationRequest registrationRequest,
                                                 BindingResult bindingResult){
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if (Objects.nonNull(errors)) return errors;

        personService.createUser(convertRegistrationRequestToPerson(registrationRequest));

        return ResponseEntity.ok(new MessageResponse(("User registered successfully")));
    }

    private Person convertRegistrationRequestToPerson(RegistrationRequest registrationRequest){
        return modelMapper.map(registrationRequest, Person.class);
    }
}
