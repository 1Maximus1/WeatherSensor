package com.example.WeatherSense.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name="Person")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="username")
    private String username;

    @Column(name = "year_of_birth")
    private int yearOfBirth;

    @Column(name="password")
    private String password;

    @Column(name = "role")
    private String role;

    @OneToMany(mappedBy = "user")
    private List<Sensor> sensors;

    public Person() {
    }

    public Person(String username, int yearOfBirth, String password, String role, List<Sensor> sensors) {
        this.username = username;
        this.yearOfBirth = yearOfBirth;
        this.password = password;
        this.role = role;
        this.sensors = sensors;
    }
}
