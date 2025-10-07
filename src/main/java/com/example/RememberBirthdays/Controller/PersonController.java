package com.example.RememberBirthdays.Controller;

import com.example.RememberBirthdays.Model.Person;
import com.example.RememberBirthdays.Repository.PersonRepository;
import com.example.RememberBirthdays.Utils.SecurityUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/people")
public class PersonController {
    @Autowired
    private PersonRepository repository;

    @PostMapping
    public Person addPerson(@RequestBody @Valid  Person person) {
        // Step 1: Get user ID from JWT
        String userId = SecurityUtils.getCurrentUserId();

        // Step 2: Attach user ID to the person
        person.setUserID(userId);

        // Step 3: Save to DB
        return repository.save(person);
    }

    @GetMapping
    public List<Person> getAllPersons(){
        String userId = SecurityUtils.getCurrentUserId();
        return repository.findAllByUserId(userId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePerson(@PathVariable Long id){
        String userId = SecurityUtils.getCurrentUserId();

        return repository.findById(id)
                .map(person -> {
                    if(!person.getUserId().equals(userId)){
                        return ResponseEntity.status(403).body("Authorization required to delete this birthday");
                    }

                    repository.delete(person);
                    return ResponseEntity.ok().build();

                })
                .orElse(ResponseEntity.notFound().build());


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
