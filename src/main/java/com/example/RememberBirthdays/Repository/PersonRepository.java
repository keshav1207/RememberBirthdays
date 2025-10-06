package com.example.RememberBirthdays.Repository;
import com.example.RememberBirthdays.Model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PersonRepository extends JpaRepository<Person, Long> {
    List<Person> findAllByUserId(String userId);
}
