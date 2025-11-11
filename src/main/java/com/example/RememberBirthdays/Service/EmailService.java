package com.example.RememberBirthdays.Service;
import com.example.RememberBirthdays.Model.Person;
import com.example.RememberBirthdays.Model.User;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendBirthdayReminder(User user, List<Person> persons) {
        String subject = "🎉 Birthday Reminder";
        String names = persons.stream()
                .map(p -> p.getFirstName() + " " + p.getLastName())
                .collect(Collectors.joining(", "));
        String text = "Hi " + user.getFirstName() + "! 🎉\n" +
                "Today is the birthday of " + names + " 🎂.\n" +
                "Don't forget to send your wishes!";


        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }
}

