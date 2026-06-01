package com.example.RememberBirthdays.Service;

import com.example.RememberBirthdays.Model.Person;
import com.example.RememberBirthdays.Model.User;
import com.example.RememberBirthdays.Repository.PersonRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BirthdayReminderServiceTest {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private BirthdayReminderService birthdayReminderService;

    private User buildUser(String userId) {
        User user = new User();
        user.setUserId(userId);
        user.setEmail(userId + "@example.com");
        user.setFirstName("Test");
        user.setLastName("User");
        return user;
    }

    private Person buildPerson(String firstName, User user) {
        Person p = new Person();
        p.setFirstName(firstName);
        p.setLastName("Smith");
        p.setBirthDate(LocalDate.of(1990, 6, 1));
        p.setUser(user);
        return p;
    }

    @Test
    void checkAndSendReminders_skipsEmailSending_whenNoBirthdaysToday() {
        // ARRANGE
        when(personRepository.findBirthdaysByDate(any(LocalDate.class))).thenReturn(List.of());

        // ACT
        birthdayReminderService.checkAndSendReminders();

        // ASSERT
        verify(emailService, never()).sendBirthdayReminder(any(), any());
    }

    @Test
    void checkAndSendReminders_sendsOneEmailPerUser_whenMultipleUsersHaveBirthdays() {
        // ARRANGE
        User user1 = buildUser("user-1");
        User user2 = buildUser("user-2");

        when(personRepository.findBirthdaysByDate(any(LocalDate.class)))
                .thenReturn(List.of(buildPerson("Alice", user1), buildPerson("Bob", user2)));

        // ACT
        birthdayReminderService.checkAndSendReminders();

        // ASSERT
        verify(emailService, times(2)).sendBirthdayReminder(any(User.class), any(List.class));
    }

    @Test
    void checkAndSendReminders_groupsBirthdaysIntoOneEmail_whenSameUserHasMultipleBirthdays() {
        // ARRANGE
        User user = buildUser("user-1");

        when(personRepository.findBirthdaysByDate(any(LocalDate.class)))
                .thenReturn(List.of(buildPerson("Alice", user), buildPerson("Charlie", user)));

        // ACT
        birthdayReminderService.checkAndSendReminders();

        // ASSERT
        verify(emailService, times(1))
                .sendBirthdayReminder(eq(user), argThat(persons -> persons.size() == 2));
    }

}