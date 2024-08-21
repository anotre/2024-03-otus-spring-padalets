package ru.otus.hw.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SecurityController {
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/forbidden")
    public String accessDeniedPage() {
        return "access-denied";
    }
}
