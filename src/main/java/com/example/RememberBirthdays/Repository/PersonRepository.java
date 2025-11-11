package com.example.RememberBirthdays.Repository;
import com.example.RememberBirthdays.Model.Person;
import com.example.RememberBirthdays.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface PersonRepository extends JpaRepository<Person, Long> {
    List<Person> findAllByUser(User user);

    @Query("SELECT p FROM Person p WHERE EXTRACT(MONTH FROM p.birthDate) = :month AND EXTRACT(DAY FROM p.birthDate) = :day")
    List<Person> findByBirthdayMonthAndDay(@Param("month") int month, @Param("day") int day);
}
