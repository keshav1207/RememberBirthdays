package com.example.RememberBirthdays.Service;

import com.example.RememberBirthdays.Model.Person;
import com.example.RememberBirthdays.Model.User;
import com.example.RememberBirthdays.Repository.PersonRepository;
import com.example.RememberBirthdays.Repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private PersonService personService;


    // This is used to clear the fake login after each test
    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    // Create a fake jwt token
    private void mockSecurityContext(String userId) {
        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "RS256")
                .subject(userId)
                .build();
        JwtAuthenticationToken auth = new JwtAuthenticationToken(jwt, List.of());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    void getAllPersonsForCurrentUser_returnsPersons_whenUserExists() {
        // ARRANGE
        String userId = "user-abc";
        mockSecurityContext(userId);

        User user = new User();
        user.setUserId(userId);

        Person p = new Person();
        p.setFirstName("John");
        p.setLastName("Doe");
        p.setBirthDate(LocalDate.of(1990, 6, 15));

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(personRepository.findAllByUser(user)).thenReturn(List.of(p));

        // ACT
        List<Person> result = personService.getAllPersonsForCurrentUser();

        // ASSERT
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFirstName()).isEqualTo("John");
    }

    @Test
    void getAllPersonsForCurrentUser_throwsException_whenUserNotFoundInDatabase() {
        // ARRANGE
        String userId = "unknown-user";
        mockSecurityContext(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // ACT + ASSERT
        assertThatThrownBy(() -> personService.getAllPersonsForCurrentUser())
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("User not found");
    }

}