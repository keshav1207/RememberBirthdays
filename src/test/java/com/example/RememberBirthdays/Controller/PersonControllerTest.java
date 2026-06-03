package com.example.RememberBirthdays.Controller;

import com.example.RememberBirthdays.Model.Person;
import com.example.RememberBirthdays.Model.User;
import com.example.RememberBirthdays.Repository.PersonRepository;
import com.example.RememberBirthdays.Service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDate;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import org.springframework.http.MediaType;
import java.util.Map;
import java.util.Optional;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(PersonController.class)
@TestPropertySource(properties = {"frontend.url=http://localhost:3000"})
class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PersonRepository repository;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JwtDecoder jwtDecoder;


    private User buildUser(String userId) {
        User user = new User();
        user.setUserId(userId);
        user.setEmail(userId + "@example.com");
        user.setFirstName("Test");
        user.setLastName("User");
        return user;
    }

    private Person buildPerson(String firstName, String lastName, User user) {
        Person p = new Person();
        p.setFirstName(firstName);
        p.setLastName(lastName);
        p.setBirthDate(LocalDate.of(1990, 5, 15));
        p.setUser(user);
        return p;
    }

    @Test
    void getPersons_returnsOkWithList_whenAuthenticated() throws Exception {
        // ARRANGE
        User user = buildUser("user-123");
        Person person = buildPerson("John", "Doe", user);

        when(userService.findOrCreateUser(any(Jwt.class))).thenReturn(user);
        when(repository.findAllByUser(user)).thenReturn(List.of(person));

        // ACT + ASSERT
        mockMvc.perform(get("/api/people").with(jwt()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].lastName").value("Doe"));
    }

    @Test
    void getPersons_returnsUnauthorized_whenNoJwt() throws Exception {
        // ACT + ASSERT
        mockMvc.perform(get("/api/people"))
                .andExpect(status().isUnauthorized());
    }


    @Test
    void addPerson_returnsNewPerson_whenRequestIsValid() throws Exception {
        // ARRANGE
        User user = buildUser("user-123");
        when(userService.findOrCreateUser(any(Jwt.class))).thenReturn(user);
        when(repository.save(any(Person.class))).thenAnswer(inv -> inv.getArgument(0));

        String body = """
            {"firstName":"Alice","lastName":"Smith","birthDate":"2000-05-15"}
            """;

        // ACT + ASSERT
        mockMvc.perform(post("/api/people")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                        .with(jwt()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Alice"))
                .andExpect(jsonPath("$.lastName").value("Smith"));
    }

    @Test
    void addPerson_returnsBadRequest_whenFirstNameIsMissing() throws Exception {
        // ARRANGE
        String body = """
            {"lastName":"Smith","birthDate":"2000-05-15"}
            """;

        // ACT + ASSERT
        mockMvc.perform(post("/api/people")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
                        .with(jwt()))
                .andExpect(status().isBadRequest());
    }


    @Test
    void deletePerson_returnsOk_whenOwnerDeletesOwnRecord() throws Exception {
        // ARRANGE
        String userId = "user-owner";
        User user = buildUser(userId);
        Person person = buildPerson("John", "Doe", user);

        when(userService.findOrCreateUser(any(Jwt.class))).thenReturn(user);
        when(repository.findById(1L)).thenReturn(Optional.of(person));

        // ACT + ASSERT
        mockMvc.perform(delete("/api/people/1")
                        .with(jwt().jwt(j -> j.subject(userId))))
                .andExpect(status().isOk());
    }

    @Test
    void deletePerson_returnsForbidden_whenNonOwnerAttemptsToDelete() throws Exception {
        // ARRANGE
        User owner = buildUser("owner-id");
        User requester = buildUser("other-user");
        Person person = buildPerson("John", "Doe", owner);

        when(userService.findOrCreateUser(any(Jwt.class))).thenReturn(requester);
        when(repository.findById(1L)).thenReturn(Optional.of(person));

        // ACT + ASSERT
        mockMvc.perform(delete("/api/people/1")
                        .with(jwt().jwt(j -> j.subject("other-user"))))
                .andExpect(status().isForbidden());
    }

    @Test
    void deletePerson_returnsOk_whenAdminDeletesAnyRecord() throws Exception {
        // ARRANGE
        User owner = buildUser("owner-id");
        User admin = buildUser("admin-id");
        Person person = buildPerson("John", "Doe", owner);

        when(userService.findOrCreateUser(any(Jwt.class))).thenReturn(admin);
        when(repository.findById(1L)).thenReturn(Optional.of(person));

        // ACT + ASSERT
        mockMvc.perform(delete("/api/people/1")
                        .with(jwt().jwt(j -> j.subject("admin-id")
                                .claim("realm_access", Map.of("roles", List.of("Admin"))))))
                .andExpect(status().isOk());
    }

    @Test
    void deletePerson_returnsNotFound_whenPersonDoesNotExist() throws Exception {
        // ARRANGE
        User user = buildUser("user-123");
        when(userService.findOrCreateUser(any(Jwt.class))).thenReturn(user);
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // ACT + ASSERT
        mockMvc.perform(delete("/api/people/99")
                        .with(jwt()))
                .andExpect(status().isNotFound());
    }
}
