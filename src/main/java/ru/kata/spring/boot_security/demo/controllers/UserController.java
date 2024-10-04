package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.kata.spring.boot_security.demo.utils.Utils;

@Controller
public class UserController {
    @GetMapping("/user")
    public String user(Model model) {
        Utils.auth(model);
        return "user";
    }
}
