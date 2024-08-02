package com.example.WeatherSense.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AuthenticationDTO {
    @NotEmpty(message = "Name doesnt have to be empty")
    @Size(min = 2,max = 100, message = "Name has to be between 2 and 100 characters")
    private String username;

    private String password;
}
