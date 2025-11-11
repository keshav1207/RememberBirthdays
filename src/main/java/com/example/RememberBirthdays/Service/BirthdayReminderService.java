package com.example.RememberBirthdays.Service;
import com.example.RememberBirthdays.Model.Person;
import com.example.RememberBirthdays.Model.User;
import com.example.RememberBirthdays.Repository.PersonRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BirthdayReminderService {

    private final PersonRepository personRepository;
    private final EmailService emailService;

    public BirthdayReminderService(PersonRepository personRepository, EmailService emailService) {
        this.personRepository = personRepository;
        this.emailService = emailService;
    }

    @Scheduled(cron = "0 0 8 * * *") // every day at 8 AM server time
    public void checkAndSendReminders() {
        LocalDate today = LocalDate.now();
        List<Person> todaysBirthdays = personRepository.findByBirthdayMonthAndDay(
                today.getMonthValue(), today.getDayOfMonth());

        Map<User, List<Person>> grouped = todaysBirthdays.stream()
                .collect(Collectors.groupingBy(Person::getUser));

        grouped.forEach((user, persons) -> {
            emailService.sendBirthdayReminder(user, persons);
        });
    }
}
