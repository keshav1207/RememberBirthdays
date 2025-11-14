package com.example.RememberBirthdays.Controller;
import com.example.RememberBirthdays.Model.Person;
import com.example.RememberBirthdays.Model.User;
import com.example.RememberBirthdays.Repository.PersonRepository;
import com.example.RememberBirthdays.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private UserRepository userRepository;


   @GetMapping("/allBirthdays")
    public List<Person> getAllPersons(){
            return personRepository.findAll();
    }

 
    @GetMapping("/allUsers")
    public List <User> getAllUsers()  {
             return userRepository.findAll();
    }
}
