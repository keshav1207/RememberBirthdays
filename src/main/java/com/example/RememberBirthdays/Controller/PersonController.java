package com.example.RememberBirthdays.Controller;


import com.example.RememberBirthdays.Model.Person;
import com.example.RememberBirthdays.Repository.PersonRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.time.LocalDate;
import java.util.List;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/people")
public class PersonController {
    @Autowired
    private PersonRepository repository;

    @PostMapping
    public Person addPerson(@RequestBody @Valid  Person person) {

        return repository.save(person);
    }

    @GetMapping
    public List<Person> getAllPersons(){
        return repository.findAll();
    }

    @DeleteMapping("/{id}")
    public void deletePerson(@PathVariable Long id){
        repository.deleteById(id);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Person> updatePerson(@PathVariable Long id, @RequestBody @Valid  Person updatedPerson){
        return repository.findById(id)
                .map(person -> {
                    person.setFirstName(updatedPerson.getFirstName());
                    person.setLastName(updatedPerson.getLastName());
                    person.setBirthDate(updatedPerson.getBirthDate());

                    repository.save(person);
                    return ResponseEntity.ok(person);
                })
                .orElse(ResponseEntity.notFound().build());
    }

}
