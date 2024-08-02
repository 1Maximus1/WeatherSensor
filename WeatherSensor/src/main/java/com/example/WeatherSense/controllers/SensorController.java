package com.example.WeatherSense.controllers;

import com.example.WeatherSense.dto.SensorDTO;
import com.example.WeatherSense.model.Person;
import com.example.WeatherSense.model.Sensor;
import com.example.WeatherSense.security.PersonDetails;
import com.example.WeatherSense.services.PersonService;
import com.example.WeatherSense.services.SensorService;
import com.example.WeatherSense.util.MeasurementException;
import com.example.WeatherSense.util.PersonNotFoundException;
import com.example.WeatherSense.util.MeasurementErrorResponse;
import com.example.WeatherSense.util.SensorValidator;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.function.Supplier;

import static com.example.WeatherSense.util.ErrorUtil.returnErrorsToClient;


@RestController
@RequestMapping("/sensors")
public class SensorController {
    private final SensorService sensorService;
    private final PersonService personService;
    private final ModelMapper mapper;
    private final SensorValidator sensorValidator;

    @Autowired
    public SensorController(SensorService sensorService, PersonService personService, ModelMapper mapper, SensorValidator sensorValidator) {
        this.sensorService = sensorService;
        this.personService = personService;
        this.mapper = mapper;
        this.sensorValidator = sensorValidator;
    }

    @PostMapping("/registration")
    public ResponseEntity<HttpStatus> registration(@RequestBody @Valid SensorDTO sensorDTO, BindingResult bindingResult){
        sensorValidator.validate(convertToSensor(sensorDTO), bindingResult);

        if (bindingResult.hasErrors()){
            returnErrorsToClient(bindingResult);
        }

        sensorService.save(convertToSensor(sensorDTO));

        return ResponseEntity.ok(HttpStatus.OK);

    }

    private Sensor convertToSensor(SensorDTO sensorDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();

        Person person = personService.findByUsername(personDetails.getUsername()).orElseThrow(() -> new PersonNotFoundException("Person with this name was not found"));

        Sensor sensor = mapper.map(sensorDTO,Sensor.class);
        sensor.setUser(person);

        return sensor;

    }

    @ExceptionHandler
    private ResponseEntity<MeasurementErrorResponse> handleException(PersonNotFoundException e){
        MeasurementErrorResponse response = new MeasurementErrorResponse(
          e.getMessage(), System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<MeasurementErrorResponse> handleException(MeasurementException e){
        MeasurementErrorResponse response = new MeasurementErrorResponse(
                e.getMessage(), System.currentTimeMillis()
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}
