package com.example.WeatherSense.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/*
* The field "name" is not a primary key (it's not a numeric key), but we are referencing it. Therefore, Hibernate requires it to be serializable.
* */

@Data
@Entity
@Table(name="Sensor")
public class Sensor implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty(message = "Name of sensor should not be empty")
    @Size(min = 2, max = 30, message = "Name of sensor should be between 2 and 30 characters")
    private String name;

    @ManyToOne
    @JoinColumn(name = "connected_user", referencedColumnName = "id")
    private Person user;

    @OneToMany(mappedBy = "sensor")
    private List<Measurement> measurements;
}
