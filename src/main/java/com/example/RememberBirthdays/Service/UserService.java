package com.example.RememberBirthdays.Service;
import com.example.RememberBirthdays.Model.User;
import com.example.RememberBirthdays.Repository.UserRepository;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public User findOrCreateUser(Jwt jwt) {
        String keycloakId = jwt.getSubject(); // this is the "sub" claim
        String email = jwt.getClaimAsString("email");
        String firstName = jwt.getClaimAsString("given_name");
        String lastName = jwt.getClaimAsString("family_name");

        return userRepository.findById(keycloakId).orElseGet(() -> {
            User user = new User();
            user.setUserId(keycloakId);
            user.setEmail(email);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            return userRepository.save(user);
        });
    }




}
