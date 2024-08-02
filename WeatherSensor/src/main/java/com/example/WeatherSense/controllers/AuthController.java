package com.example.WeatherSense.controllers;

import com.example.WeatherSense.dto.AuthenticationDTO;
import com.example.WeatherSense.dto.PersonDTO;
import com.example.WeatherSense.model.Person;
import com.example.WeatherSense.security.JWTUtil;
import com.example.WeatherSense.services.RegistrationService;
import com.example.WeatherSense.util.MeasurementErrorResponse;
import com.example.WeatherSense.util.MeasurementException;
import com.example.WeatherSense.util.PersonValidator;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.example.WeatherSense.util.ErrorUtil.returnErrorsToClient;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final PersonValidator personValidator;
    private final RegistrationService registrationService;
    private final ModelMapper mapper;
    private final JWTUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthController(PersonValidator personValidator, RegistrationService registrationService, ModelMapper mapper, JWTUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.personValidator = personValidator;
        this.registrationService = registrationService;
        this.mapper = mapper;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }


    @PostMapping("/registration")
    public Map<String, String> performRegistration(@RequestBody @Valid PersonDTO personDTO, BindingResult bindingResult){
        Person person = convertToPerson(personDTO);
        personValidator.validate(person,bindingResult);

        if (bindingResult.hasErrors()){
            returnErrorsToClient(bindingResult);
        }

        registrationService.register(person);
        String token = jwtUtil.generateToken(person.getUsername());

        return Map.of("jwt-token",token);
    }

    @PostMapping("/login")
    private Map<String,String> performLogin(@RequestBody @Valid AuthenticationDTO authenticationDTO, BindingResult bindingResult){
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(authenticationDTO.getUsername(),authenticationDTO.getPassword());

        try {
            authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        }catch (BadCredentialsException e){
            throw new BadCredentialsException("Invalid credentials");
        }

        String token = jwtUtil.generateToken(authenticationDTO.getUsername());

        return Map.of("jwt-token", token);
    }

    private Person convertToPerson(PersonDTO personDTO){
        return mapper.map(personDTO,Person.class);
    }

    @ExceptionHandler
    private ResponseEntity<MeasurementErrorResponse> handleException(MeasurementException e){
        MeasurementErrorResponse response = new MeasurementErrorResponse(
                e.getMessage(), System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<MeasurementErrorResponse> handleException(BadCredentialsException e) {
        MeasurementErrorResponse response = new MeasurementErrorResponse(
                e.getMessage(), System.currentTimeMillis()
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
