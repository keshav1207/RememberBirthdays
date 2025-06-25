package com.example.RememberBirthdays.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;


import java.time.LocalDate;

@Entity

public class Person{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotNull(message = "Birth date is required")
    @Past(message = "Birth date must be in the past")
    private LocalDate birthDate;

    public Long getId() {
        return id;
    }

    public String getFirstName(){
        return this.firstName;
    }

    public String getLastName(){
        return this.lastName;
    }

    public LocalDate getBirthDate(){
        return this.birthDate;
    }

    public void setFirstName(String firstname){
        this.firstName = firstname;
    }

    public void setLastName(String lastname){
        this.lastName = lastname;
    }

    public void setBirthDate(LocalDate date){
         this.birthDate = date;
    }
}
