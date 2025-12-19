package com.example.RememberBirthdays.Service;

import com.example.RememberBirthdays.Model.Person;
import com.example.RememberBirthdays.Model.User;
import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);


    @Value("${sendgrid.api-key}")
    private String sendGridApiKey;


    @Value("${sendgrid.from-email}")
    private String fromEmail;

    // Runs once at startup to verify config
    @PostConstruct
    public void checkConfig() {
        log.info("SendGrid API key present: {}", sendGridApiKey != null && !sendGridApiKey.isBlank());
        log.info("SendGrid FROM email: {}", fromEmail);
    }

    public void sendBirthdayReminder(User user, List<Person> persons) {

        if (sendGridApiKey == null || sendGridApiKey.isBlank()) {
            log.error("SendGrid API key is missing — email not sent");
            return;
        }

        String subject = "🎉 Birthday Reminder";

        String names = persons.stream()
                .map(p -> p.getFirstName() + " " + p.getLastName())
                .collect(Collectors.joining(", "));

        String text = "Hi " + user.getFirstName() + "! 🎉\n\n"
                + "Today is the birthday of:\n"
                + names + " 🎂\n\n"
                + "Don't forget to send your wishes!";

        Mail mail = new Mail(
                new Email(fromEmail),
                subject,
                new Email(user.getEmail()),
                new Content("text/plain", text)
        );

        SendGrid sg = new SendGrid(sendGridApiKey);
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            Response response = sg.api(request);

            log.info("SendGrid status: {}", response.getStatusCode());
            log.info("SendGrid response body: {}", response.getBody());

        } catch (IOException e) {
            log.error("Failed to send birthday email via SendGrid", e);
        }
    }
}