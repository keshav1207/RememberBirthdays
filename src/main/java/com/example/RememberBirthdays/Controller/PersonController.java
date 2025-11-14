package com.example.RememberBirthdays.Controller;
import com.example.RememberBirthdays.Model.Person;
import com.example.RememberBirthdays.Model.User;
import com.example.RememberBirthdays.Repository.PersonRepository;
import com.example.RememberBirthdays.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/people")
public class PersonController {

    @Autowired
    private PersonRepository repository;
    @Autowired
    private UserService userservice;

    @PostMapping
    public Person addPerson(@RequestBody @Valid  Person person, @org.springframework.security.core.annotation.AuthenticationPrincipal Jwt jwt) {
        User user = userservice.findOrCreateUser(jwt);
        person.setUser(user);

        return repository.save(person);

    }

    @GetMapping
    public List<Person> getAllPersons(@org.springframework.security.core.annotation.AuthenticationPrincipal Jwt jwt){
        User user = userservice.findOrCreateUser(jwt);
        return repository.findAllByUser(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePerson(@PathVariable Long id, @org.springframework.security.core.annotation.AuthenticationPrincipal Jwt jwt){
        User user  = userservice.findOrCreateUser(jwt);

        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
        Set<String> realmRoles = new HashSet<>();
        if (realmAccess != null && realmAccess.containsKey("roles")) {
            realmRoles.addAll((List<String>) realmAccess.get("roles"));
        }

        boolean isAdmin = realmRoles.contains("Admin");

        return repository.findById(id)
                .map(person -> {
                    if(!(person.getUser().getUserId().equals(user.getUserId()) ) && !isAdmin){
                        return ResponseEntity.status(403).body("Authorization required to delete this birthday");
                    }

                    repository.delete(person);
                    return ResponseEntity.ok().build();

                })
                .orElse(ResponseEntity.notFound().build());


    }


    @PutMapping("/{id}")
    public ResponseEntity<?> updatePerson(@PathVariable Long id, @RequestBody @Valid  Person updatedPerson, @org.springframework.security.core.annotation.AuthenticationPrincipal Jwt jwt){
        User user = userservice.findOrCreateUser(jwt);
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
        Set<String> realmRoles = new HashSet<>();
        if (realmAccess != null && realmAccess.containsKey("roles")) {
            realmRoles.addAll((List<String>) realmAccess.get("roles"));
        }

        boolean isAdmin = realmRoles.contains("Admin");
        return repository.findById(id)
                .map(person -> {

                    if(!(person.getUser().getUserId().equals(user.getUserId()) ) && !isAdmin){
                        System.out.println("isAdmin = " + isAdmin);
                        return ResponseEntity.status(403).body("Authorization required to edit this birthday");
                    }

                    person.setFirstName(updatedPerson.getFirstName());
                    person.setLastName(updatedPerson.getLastName());
                    person.setBirthDate(updatedPerson.getBirthDate());

                    repository.save(person);
                    return ResponseEntity.ok(person);
                })
                .orElse(ResponseEntity.notFound().build());
    }

}
