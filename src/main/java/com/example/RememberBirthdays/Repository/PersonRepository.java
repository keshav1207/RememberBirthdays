package com.example.RememberBirthdays.Repository;
import com.example.RememberBirthdays.Model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {}
