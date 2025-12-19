
package com.example.RememberBirthdays.Service;

import com.example.RememberBirthdays.Model.Person;
import com.example.RememberBirthdays.Model.User;
import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmailService {

    @Value("${SENDGRID_API_KEY}")
    private String sendGridApiKey;

    @Value("${SENDGRID_FROM_EMAIL}")
    private String fromEmail;

    public void sendBirthdayReminder(User user, List<Person> persons) {
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
           
        } catch (IOException ex) {
           
            ex.printStackTrace();
        }
    }
}

