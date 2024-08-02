package com.example.WeatherSense.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SensorDTO {
    @NotEmpty(message = "Name of sensor should not be empty")
    @Size(min = 2, max = 30, message = "Name of sensor should be between 2 and 30 characters")
    private String name;
}
