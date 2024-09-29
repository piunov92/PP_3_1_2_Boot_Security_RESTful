package ru.kata.spring.boot_security.demo.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kata.spring.boot_security.demo.models.UserForm;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;
import ru.kata.spring.boot_security.demo.services.UserService;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserRepository userRepository;
    private final UserService userService;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminController(UserRepository userRepository, UserService userService, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String allUsers(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "/admin/index";
    }

    @GetMapping("new")
    public String newUser(@ModelAttribute("userForm") UserForm userForm) {
        return "/admin/new";
    }

    @PostMapping("new")
    public String registerUser(@ModelAttribute("userForm")  @Valid UserForm userForm, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "/admin/new";
        }
        try {
            List<String> roleNames = Arrays.asList(userForm.getRoles().split(","));
            userService.newUser(userForm.getUsername(), userForm.getPassword(), userForm.getEmail(), roleNames);
            return "redirect:/admin";
        } catch (Exception e) {
            model.addAttribute("error", "Error when filling the form, please try again");
            return "/admin/new";
        }
    }
}
