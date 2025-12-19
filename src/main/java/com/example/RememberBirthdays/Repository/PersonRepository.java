package com.example.RememberBirthdays.Repository;

import com.example.RememberBirthdays.Model.Person;
import com.example.RememberBirthdays.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    List<Person> findAllByUser(User user);

    // today's birthdays query using a LocalDate parameter
    @Query("SELECT p FROM Person p WHERE FUNCTION('DATE', p.birthDate) = :today")
    List<Person> findBirthdaysByDate(@Param("today") LocalDate today);
}
