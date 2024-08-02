package com.example.WeatherSense.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class MeasurementDTO {
    @NotNull
    @Min(value = -100, message = "Temperature have to be grater than -100")
    @Max(value = 100, message = "Temperature have to be lower than 100")
    private Double value;

    @NotNull
    private Boolean raining;

    @NotNull
    private SensorDTO sensor;
}
