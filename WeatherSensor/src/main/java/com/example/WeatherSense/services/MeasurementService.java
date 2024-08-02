package com.example.WeatherSense.services;

import com.example.WeatherSense.model.Measurement;
import com.example.WeatherSense.repositories.MeasurementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class MeasurementService {
    private final MeasurementRepository measurementRepository;

    @Autowired
    public MeasurementService(MeasurementRepository measurementRepository) {
        this.measurementRepository = measurementRepository;
    }

    @Transactional
    public void save(Measurement measurement){
        measurement.setReceivedAt(LocalDateTime.now());
        measurementRepository.save(measurement);
    }

    public List<Measurement> allMeasurement(){
        return measurementRepository.findAll();
    }

    public List<Measurement> findByRainingIsTrue() {
        return measurementRepository.findByRainingIsTrue();
    }

    public Long rainyDaysCount() {
        return (long) findByRainingIsTrue().size();
    }

}
