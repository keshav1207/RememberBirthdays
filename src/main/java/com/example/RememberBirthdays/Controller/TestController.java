package com.example.RememberBirthdays.Controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class TestController {

    @GetMapping("/test/hello")
    public String publicHello() {
        return "Public endpoint";
    }

    @GetMapping("/secure/hello")
    public String secureHello() {
        return "Secure endpoint";
    }
}
