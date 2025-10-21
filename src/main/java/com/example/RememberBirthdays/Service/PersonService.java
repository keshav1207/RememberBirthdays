package com.example.RememberBirthdays.Service;
import com.example.RememberBirthdays.Model.Person;
import com.example.RememberBirthdays.Model.User;
import com.example.RememberBirthdays.Repository.PersonRepository;
import com.example.RememberBirthdays.Repository.UserRepository;
import com.example.RememberBirthdays.Utils.SecurityUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonService {

    private final PersonRepository personRepository;
    private final UserRepository userRepository;

    public PersonService(PersonRepository personRepository, UserRepository userRepository) {
        this.personRepository = personRepository;
        this.userRepository = userRepository;
    }

     public List<Person> getAllPersonsForCurrentUser(){
         String userId = SecurityUtils.getCurrentUserId();
         User user = userRepository.findById(userId)
                 .orElseThrow(() -> new RuntimeException("User not found: " + userId));

         return personRepository.findAllByUser(user);
     }
}
