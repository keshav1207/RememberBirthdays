package com.example.RememberBirthdays.Controller;


import com.example.RememberBirthdays.Model.Person;
import com.example.RememberBirthdays.Repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/people")
public class PersonController {
    @Autowired
    private PersonRepository repository;

    @PostMapping
    public Person addPerson(@RequestBody Person person) {
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


}
