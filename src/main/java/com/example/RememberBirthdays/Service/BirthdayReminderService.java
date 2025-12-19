package com.example.RememberBirthdays.Service;

import com.example.RememberBirthdays.Model.Person;
import com.example.RememberBirthdays.Model.User;
import com.example.RememberBirthdays.Repository.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BirthdayReminderService {

    private static final Logger log = LoggerFactory.getLogger(BirthdayReminderService.class);

    private final PersonRepository personRepository;
    private final EmailService emailService;

    public BirthdayReminderService(PersonRepository personRepository, EmailService emailService) {
        this.personRepository = personRepository;
        this.emailService = emailService;
    }

    @Scheduled(cron = "0 * * * * *") // every minute (for testing)
    public void checkAndSendReminders() {

        List<Person> todaysBirthdays = personRepository.findTodayBirthdays();

        log.info("Birthday reminder job ran — found {} birthdays today", todaysBirthdays.size());

        if (todaysBirthdays.isEmpty()) {
            log.info("No birthdays today — skipping email sending");
            return;
        }

        Map<User, List<Person>> grouped = todaysBirthdays.stream()
                .collect(Collectors.groupingBy(Person::getUser));

        grouped.forEach((user, persons) -> {
            log.info("Sending birthday reminder to user: {} ({})", user.getFirstName(), user.getEmail());
            emailService.sendBirthdayReminder(user, persons);
        });
    }
}
