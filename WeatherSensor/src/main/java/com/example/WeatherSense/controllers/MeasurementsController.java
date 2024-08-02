package com.example.WeatherSense.controllers;

import com.example.WeatherSense.dto.MeasurementDTO;
import com.example.WeatherSense.dto.MeasurementsResponse;
import com.example.WeatherSense.model.Measurement;
import com.example.WeatherSense.model.Sensor;
import com.example.WeatherSense.services.MeasurementService;
import com.example.WeatherSense.services.SensorService;
import com.example.WeatherSense.util.*;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


import static com.example.WeatherSense.util.ErrorUtil.returnErrorsToClient;

@RestController
@RequestMapping("/measurements")
public class MeasurementsController {
    private final MeasurementService measurementService;
    private final MeasurementValidator measurementValidator;
    private final ModelMapper mapper;
    private final SensorService sensorService;

    @Autowired
    public MeasurementsController(MeasurementService measurementService, MeasurementValidator measurementValidator, ModelMapper mapper, SensorService sensorService) {
        this.measurementService = measurementService;
        this.measurementValidator = measurementValidator;
        this.mapper = mapper;
        this.sensorService = sensorService;
    }

    @PostMapping("/add")
    public ResponseEntity<HttpStatus> add(@RequestBody @Valid MeasurementDTO measurementDTO, BindingResult bindingResult) {
        measurementValidator.validate(convertToMeasurement(measurementDTO), bindingResult);
        if (bindingResult.hasErrors()) {
            returnErrorsToClient(bindingResult);
        }
        measurementService.save(convertToMeasurement(measurementDTO));
        return ResponseEntity.ok(HttpStatus.OK);
    }


    @GetMapping()
    public MeasurementsResponse allInformation() {
        return new MeasurementsResponse(measurementService.allMeasurement().stream().map(this::convertToMeasurementDTO).toList());
    }

    @GetMapping("/rainyDaysCount")
    public Long rainyDaysCount(){
        return measurementService.rainyDaysCount();
    }

    private MeasurementDTO convertToMeasurementDTO(Measurement m) {
        return mapper.map(m, MeasurementDTO.class);
    }

    private Measurement convertToMeasurement(MeasurementDTO weatherDTO) {
        Sensor sensor = sensorService.findByName(weatherDTO.getSensor().getName()).orElseThrow(SensorNotFoundException::new);
        Measurement measurement = mapper.map(weatherDTO, Measurement.class);
        measurement.setSensor(sensor);
        return measurement;
    }

    @ExceptionHandler
    private ResponseEntity<MeasurementErrorResponse> handleException(SensorNotFoundException e){
        MeasurementErrorResponse response = new MeasurementErrorResponse(
                "Sensor with this name was not found", System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<MeasurementErrorResponse> handleException(MeasurementException e) {
        MeasurementErrorResponse response = new MeasurementErrorResponse(
                e.getMessage(), System.currentTimeMillis()
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<MeasurementErrorResponse> handleException(PersonNotFoundException e) {
        MeasurementErrorResponse response = new MeasurementErrorResponse(
                "Person with this name was not found", System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}