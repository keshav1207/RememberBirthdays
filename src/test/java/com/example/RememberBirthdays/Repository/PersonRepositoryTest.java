package com.example.RememberBirthdays.Repository;

import com.example.RememberBirthdays.Model.Person;
import com.example.RememberBirthdays.Model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class PersonRepositoryTest {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private TestEntityManager entityManager;

    private User persistUser(String userId) {
        User user = new User();
        user.setUserId(userId);
        user.setEmail(userId + "@example.com");
        user.setFirstName("Test");
        user.setLastName("User");
        entityManager.persist(user);
        return user;
    }

    private Person persistPerson(String firstName, String lastName, User user) {
        Person p = new Person();
        p.setFirstName(firstName);
        p.setLastName(lastName);
        p.setBirthDate(LocalDate.of(1990, 6, 15));
        p.setUser(user);
        entityManager.persist(p);
        return p;
    }

    @Test
    void findAllByUser_returnsOnlyPersonsBelongingToThatUser() {
        // ARRANGE
        User user1 = persistUser("user-1");
        User user2 = persistUser("user-2");
        persistPerson("Alice", "Smith", user1);
        persistPerson("Bob", "Jones", user1);
        persistPerson("Charlie", "Brown", user2);
        entityManager.flush();

        // ACT
        List<Person> result = personRepository.findAllByUser(user1);

        // ASSERT
        assertThat(result).hasSize(2);
        assertThat(result).extracting(Person::getFirstName)
                .containsExactlyInAnyOrder("Alice", "Bob");
    }

    @Test
    void findAllByUser_returnsEmptyList_whenUserHasNoPersons() {
        // ARRANGE
        User user = persistUser("user-no-birthdays");
        entityManager.flush();

        // ACT
        List<Person> result = personRepository.findAllByUser(user);

        // ASSERT
        assertThat(result).isEmpty();
    }

    @Test
    void save_persistsPersonAndAssignsGeneratedId() {
        // ARRANGE
        User user = persistUser("user-save-test");
        entityManager.flush();

        Person p = new Person();
        p.setFirstName("David");
        p.setLastName("Lee");
        p.setBirthDate(LocalDate.of(1985, 3, 22));
        p.setUser(user);

        // ACT
        Person saved = personRepository.save(p);

        // ASSERT
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getFirstName()).isEqualTo("David");
    }

}