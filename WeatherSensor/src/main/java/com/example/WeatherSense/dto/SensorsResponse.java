package com.example.WeatherSense.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class SensorsResponse {
    private List<SensorDTO> sensors;
}
