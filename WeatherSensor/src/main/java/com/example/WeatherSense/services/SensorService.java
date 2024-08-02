package com.example.WeatherSense.services;


import com.example.WeatherSense.model.Person;
import com.example.WeatherSense.model.Sensor;
import com.example.WeatherSense.repositories.SensorRepository;
import com.example.WeatherSense.util.SensorNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class SensorService {
    private final SensorRepository sensorRepository;

    @Autowired
    public SensorService(SensorRepository sensorRepository) {
        this.sensorRepository = sensorRepository;
    }

    @Transactional
    public void save(Sensor sensor){
        sensorRepository.save(sensor);
    }

    public Optional<Sensor> findByName(String name){
        return sensorRepository.findByName(name);
    }

    public List<Sensor> findAll(){
        return sensorRepository.findAll();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional
    public void deleteById(Integer id){
        sensorRepository.deleteById(id);
    }

    public Sensor findSensorByID(Integer id){
        return sensorRepository.findById(id).orElseThrow(SensorNotFoundException::new);
    }
}
