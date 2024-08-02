package com.example.WeatherSense.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PersonDTO {
    @NotEmpty(message = "Name doesnt have to be empty")
    @Size(min = 2,max = 100, message = "Name has to be between 2 and 100 characters")
    private String username;

    @Min(value = 1900, message = "Year has to be greater than 1900")
    private Integer yearOfBirth;

    private String password;
}
