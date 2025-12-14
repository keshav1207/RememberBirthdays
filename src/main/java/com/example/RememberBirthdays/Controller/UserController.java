package com.example.RememberBirthdays.Controller;
import com.example.RememberBirthdays.DTO.CreateUserRequest;
import com.example.RememberBirthdays.Model.User;
import com.example.RememberBirthdays.Repository.UserRepository;
import com.example.RememberBirthdays.Service.KeycloakAdminService;
import com.example.RememberBirthdays.Service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



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

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUser(@PathVariable  String userId){
        return userRepository.findByUserId(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @PostMapping
    public ResponseEntity<User> addUser(@RequestBody @Valid CreateUserRequest request){
        String keycloakUserId = keycloakAdminService.createUserInKeycloak(
                request.getFirstName(),
                request.getLastName(),
                request.getEmail(),
                request.getPassword()
        );

        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setUserId(keycloakUserId);

        User savedUser = userRepository.save(user);
        return  ResponseEntity.ok(savedUser);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<User> editUser(@PathVariable String userId, @RequestBody @Valid User updatedUser){
        String newFirstName = updatedUser.getFirstName();
        String newLastName = updatedUser.getLastName();
        String newEmail = updatedUser.getEmail();
        return userRepository.findByUserId(userId)
                .map(existingUser -> {

                    existingUser.setFirstName(newFirstName);
                    existingUser.setLastName(newLastName);
                    keycloakAdminService.updateUserInKeycloak(userId,newFirstName,newLastName,newEmail);
                    existingUser.setEmail(newEmail);
                    User savedUser = userRepository.save(existingUser);

                    return ResponseEntity.ok(savedUser);
                })
                .orElse(ResponseEntity.notFound().build());

    }
    @DeleteMapping("/{userId}")
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
