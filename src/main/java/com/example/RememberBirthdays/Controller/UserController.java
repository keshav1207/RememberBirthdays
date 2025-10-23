package com.example.RememberBirthdays.Controller;
import com.example.RememberBirthdays.Model.User;
import com.example.RememberBirthdays.Repository.UserRepository;
import com.example.RememberBirthdays.Service.KeycloakAdminService;
import com.example.RememberBirthdays.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userservice;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private KeycloakAdminService keycloakAdminService;

    @GetMapping({"/id"})
    public ResponseEntity<User> getUser(@PathVariable  String userId){
        return userRepository.findByUserId(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @PostMapping
    public ResponseEntity<User> addUser(@RequestBody @Valid User user, String password){
        String keycloakUserId = keycloakAdminService.createUserInkeycloak(
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                password
        );

        user.setUserId(keycloakUserId);
        User savedUser = userRepository.save(user);
        return  ResponseEntity.ok(savedUser);
    }

    @PutMapping({"{/userId}"})
    public ResponseEntity<User> editUser(@PathVariable String userId, @RequestBody @Valid User updatedUser){
        return userRepository.findByUserId(userId)
                .map(existingUser -> {
                    existingUser.setFirstName(updatedUser.getFirstName());
                    existingUser.setLastName(updatedUser.getLastName());
                    existingUser.setEmail(updatedUser.getEmail());
                    User savedUser = userRepository.save(existingUser);
                    return ResponseEntity.ok(savedUser);
                })
                .orElse(ResponseEntity.notFound().build());

    }
    @DeleteMapping({"/id"})
    public ResponseEntity<Void> deleteUser(@PathVariable String userId){

        if (userRepository.existsById(userId)){
                keycloakAdminService.deleteUserFromKeycloak(userId);
                userRepository.deleteById(userId);
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.notFound().build();
            }
    }



}
