package com.example.RememberBirthdays.Service;
import com.example.RememberBirthdays.Model.Person;
import com.example.RememberBirthdays.Repository.PersonRepository;
import com.example.RememberBirthdays.Utils.SecurityUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonService {

    private final PersonRepository personRepository;


    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

     public List<Person> getAllPersonsForCurrentUser(){
         String userId = SecurityUtils.getCurrentUserId();
         return personRepository.findAllByUserId(userId);
     }
}
