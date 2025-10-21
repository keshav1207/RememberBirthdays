package com.example.RememberBirthdays.Model;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import jakarta.persistence.Table;

@Entity
@Table(name = "app_user") // Renaming table because user is a reserved keyword in postgres causing issue
public class User {
    @Id
    private String userId;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Email is required")
    private String email;

    // Defines a one-to-many relationship where one User can have multiple Person entries;
    // 'mappedBy' indicates Person owns the relationship via its 'user' field;
    // CascadeType.ALL ensures changes to User (save/delete) propagate to associated Persons.
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Person> people;

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPeople(List<Person> people) {
        this.people = people;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUserId() {
        return userId;
    }
}

