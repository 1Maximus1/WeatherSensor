package com.example.WeatherSense.services;

import com.example.WeatherSense.model.Person;
import com.example.WeatherSense.repositories.PeopleRepository;
import com.example.WeatherSense.util.PersonNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

@Service
@Transactional(readOnly = true)
public class PersonService {
    private final PeopleRepository peopleRepository;

    @Autowired
    public PersonService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    public Person findById(Integer userId){
        return peopleRepository.findById(userId).orElseThrow(() -> new PersonNotFoundException("Person with this id was not found"));
    }

    public Optional<Person> findByUsername(String username){
        return peopleRepository.findByUsername(username);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional
    public void performOperationOnPerson(Integer id, Consumer<Person> cons){
        Optional<Person> personOptional = peopleRepository.findById(id);
        if (personOptional.isPresent()){
            Person person = personOptional.get();
            cons.accept(person);
            peopleRepository.save(person);
        }else {
            throw new PersonNotFoundException("Person with this id was not found");
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional
    public void deletePerson(Integer id){
        peopleRepository.deleteById(id);
    }
}
