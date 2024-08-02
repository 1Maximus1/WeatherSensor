package com.example.WeatherSense.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;

@Component
@Data
@Entity
@Table(name="Measurement")
public class Measurement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Min(value = -100, message = "Temperature have to be grater than -100")
    @Max(value = 100, message = "Temperature have to be lower than 100")
    private Double value;

    @NotNull
    private Boolean raining;

    @NotNull
    @Column(name="measurement_date_time")
    private LocalDateTime receivedAt;

    @NotNull
    @ManyToOne
    @JoinColumn(name="sensor", referencedColumnName = "name")
    private Sensor sensor;

}
