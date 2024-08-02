package com.example.WeatherSense.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class MeasurementsResponse {
    private List<MeasurementDTO> measurements;

}
