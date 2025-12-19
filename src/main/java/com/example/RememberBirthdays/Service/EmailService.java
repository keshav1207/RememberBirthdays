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

    @Value("${sendgrid.api.key}")
    private String sendGridApiKey;

    @Value("${sendgrid.from.email}")
    private String fromEmail;

    @PostConstruct
    public void checkConfig() {
        log.info("SendGrid key present: {}", sendGridApiKey != null && !sendGridApiKey.isBlank());
        log.info("From email: {}", fromEmail);
    }

    public void sendBirthdayReminder(User user, List<Person> persons) {
        if (sendGridApiKey == null || sendGridApiKey.isBlank()) {
            log.error("SendGrid API key is not configured!");
            return;
        }
        if (fromEmail == null || fromEmail.isBlank()) {
            log.error("SendGrid from email is not configured!");
            return;
        }

        String subject = "🎉 Birthday Reminder";
        String names = persons.stream()
                .map(p -> p.getFirstName() + " " + p.getLastName())
                .collect(Collectors.joining(", "));
        String text = "Hi " + user.getFirstName() + "! 🎉\n" +
                "Today is the birthday of " + names + " 🎂.\n" +
                "Don't forget to send your wishes!";

        Email from = new Email(fromEmail);
        Email to = new Email(user.getEmail());
        Content content = new Content("text/plain", text);
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid(sendGridApiKey);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);

            log.info("Email sent to {} — Status: {}", user.getEmail(), response.getStatusCode());
        } catch (IOException ex) {
            log.error("Error sending email to {}", user.getEmail(), ex);
        }
    }
}