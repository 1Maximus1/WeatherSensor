package com.example.WeatherSense.controllers;

import com.example.WeatherSense.dto.PersonDTO;
import com.example.WeatherSense.dto.SensorDTO;
import com.example.WeatherSense.dto.SensorsResponse;
import com.example.WeatherSense.model.Person;
import com.example.WeatherSense.model.Sensor;
import com.example.WeatherSense.services.PersonService;
import com.example.WeatherSense.services.SensorService;
import com.example.WeatherSense.util.MeasurementErrorResponse;
import com.example.WeatherSense.util.PersonNotFoundException;
import com.example.WeatherSense.util.SensorNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final SensorService sensorService;
    private final ModelMapper mapper;
    private final PersonService personService;

    @Autowired
    public AdminController(SensorService sensorService, ModelMapper mapper, PersonService personService) {
        this.sensorService = sensorService;
        this.mapper = mapper;
        this.personService = personService;
    }

    @GetMapping("/allSensors")
    public SensorsResponse sensorsResponse(){
        return new SensorsResponse(sensorService.findAll().stream().map(this::convertToSensorDTO).toList());
    }

    @GetMapping("/getSensorById/{id}")
    public SensorDTO getSensorById(@PathVariable Integer id){
        return convertToSensorDTO(sensorService.findSensorByID(id));
    }

    @GetMapping("/getPersonById/{id}")
    public PersonDTO getPersonById(@PathVariable("id") Integer id){
        return convertToPersonDTO(personService.findById(id));
    }

    @PostMapping("/delSensorById/{id}")
    public void deleteSensor(@PathVariable("id") Integer id){
        sensorService.deleteById(id);
    }

    @PostMapping("/delPersonById/{id}")
    public void deletePerson(@PathVariable("id") Integer id){
        personService.deletePerson(id);
    }

    @PostMapping("/appointAdminById/{id}")
    private void appointAdminById(@PathVariable("id") Integer id){
        personService.performOperationOnPerson(id, person -> person.setRole("ROLE_ADMIN"));
    }

    private SensorDTO convertToSensorDTO(Sensor sensor){
       return mapper.map(sensor, SensorDTO.class);
    }

    private PersonDTO convertToPersonDTO(Person person){
        return mapper.map(person, PersonDTO.class);
    }


    @ExceptionHandler
    private ResponseEntity<MeasurementErrorResponse> handleException(SensorNotFoundException e){
        MeasurementErrorResponse response = new MeasurementErrorResponse(
                "Sensor with this id was not found", System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<MeasurementErrorResponse> handleException(PersonNotFoundException e){
        MeasurementErrorResponse response = new MeasurementErrorResponse(
                "Person with this id was not found", System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}
