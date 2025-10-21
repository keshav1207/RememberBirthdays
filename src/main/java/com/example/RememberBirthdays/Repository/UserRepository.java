package com.example.RememberBirthdays.Repository;
import com.example.RememberBirthdays.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface  UserRepository extends JpaRepository<User,String> {
            User findByEmail(String email);
    }


