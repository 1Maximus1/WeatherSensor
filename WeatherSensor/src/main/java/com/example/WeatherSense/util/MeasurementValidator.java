package com.example.WeatherSense.util;

import com.example.WeatherSense.dto.MeasurementDTO;
import com.example.WeatherSense.dto.SensorDTO;
import com.example.WeatherSense.model.Measurement;
import com.example.WeatherSense.model.Sensor;
import com.example.WeatherSense.repositories.SensorRepository;
import com.example.WeatherSense.services.MeasurementService;
import jakarta.validation.executable.ValidateOnExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class MeasurementValidator implements Validator {
    private final SensorRepository sensorRepository;

    @Autowired
    public MeasurementValidator(SensorRepository sensorRepository) {
        this.sensorRepository = sensorRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Measurement.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Measurement measurement = (Measurement) target;
        if (measurement.getSensor()==null){
            return;
        }

        if(sensorRepository.findByName(measurement.getSensor().getName()).isEmpty()){
            errors.rejectValue("sensor", "", "Sensor doesn't exist!");
        }
    }
}
