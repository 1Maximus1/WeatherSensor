package com.example.WeatherSense.services;


import com.example.WeatherSense.model.Person;
import com.example.WeatherSense.repositories.PeopleRepository;
import com.example.WeatherSense.security.PersonDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PersonDetailsService implements UserDetailsService {
    private final PeopleRepository peopleRepository;

    public PersonDetailsService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Person> personOptional = peopleRepository.findByUsername(username);

        if (personOptional.isEmpty()){
            throw new UsernameNotFoundException("User not found!");
        }
        return new PersonDetails(personOptional.get());
    }
}

