package com.example.RememberBirthdays.Service;

import com.example.RememberBirthdays.Model.User;
import com.example.RememberBirthdays.Repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.jwt.Jwt;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


//This tells JUnit before each test , let Mockito set up all mock or inject Mock fields
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private Jwt buildJwt(String subject, String email, String firstName, String lastName) {
        return Jwt.withTokenValue("test-token")
                .header("alg", "RS256")
                .subject(subject)
                .claim("email", email)
                .claim("given_name", firstName)
                .claim("family_name", lastName)
                .build();
    }

    @Test
    void findOrCreateUser_returnsExistingUser_whenUserAlreadyInDatabase() {
        // ARRANGE
        String keycloakId = "kc-user-123";
        User existing = new User();
        existing.setUserId(keycloakId);
        existing.setEmail("john@example.com");
        existing.setFirstName("John");
        existing.setLastName("Doe");

        Jwt jwt = buildJwt(keycloakId, "john@example.com", "John", "Doe");
        // if findById is called with this ID, return this user
        when(userRepository.findById(keycloakId)).thenReturn(Optional.of(existing));

        // ACT
        User result = userService.findOrCreateUser(jwt);

        // ASSERT
        assertThat(result.getUserId()).isEqualTo(keycloakId);
        assertThat(result.getEmail()).isEqualTo("john@example.com");

        // We test that since this is an existing user, the save to repository is never called.
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void findOrCreateUser_createsAndSavesNewUser_whenUserNotInDatabase() {
        // ARRANGE
        String keycloakId = "kc-new-456";
        Jwt jwt = buildJwt(keycloakId, "jane@example.com", "Jane", "Smith");

        when(userRepository.findById(keycloakId)).thenReturn(Optional.empty());

        //When save is called with any User object, return that same User object back.
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        // ACT
        User result = userService.findOrCreateUser(jwt);

        // ASSERT
        assertThat(result.getUserId()).isEqualTo(keycloakId);
        assertThat(result.getEmail()).isEqualTo("jane@example.com");
        assertThat(result.getFirstName()).isEqualTo("Jane");
        assertThat(result.getLastName()).isEqualTo("Smith");
        verify(userRepository).save(any(User.class));
    }

}

