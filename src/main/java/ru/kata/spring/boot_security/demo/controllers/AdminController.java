package ru.kata.spring.boot_security.demo.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.models.UserForm;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;
import ru.kata.spring.boot_security.demo.services.UserService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserRepository userRepository;
    private final UserService userService;

    @Autowired
    public AdminController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @ModelAttribute("allRoles")
    public List<String> getAllRoles() {
        return Arrays.asList("ROLE_USER", "ROLE_ADMIN");
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
    public String addUser(@ModelAttribute("userForm") @Valid UserForm userForm, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "/admin/new";
        }
        try {
            userService.newUser(userForm);
            return "redirect:/admin";
        } catch (Exception e) {
            model.addAttribute("error", "Error when filling the form, please try again");
            return "/admin/new";
        }
    }

    @GetMapping("edit")
    public String editUser(@RequestParam("id") Long id, Model model) {
        User user = userService.findUserById(id);

        UserForm userForm = new UserForm();
        userForm.setUserId(user.getId());
        userForm.setUsername(user.getUsername());
        userForm.setEmail(user.getEmail());
        userForm.setRoles(new ArrayList<>(user.getRoleNames()));

        model.addAttribute("userForm", userForm);
        return "/admin/edit";
    }

    @PostMapping("edit")
    public String updateUser(@RequestParam("id") Long id, @ModelAttribute UserForm userForm, Model model) {
        try {
            userService.updateUser(userForm , id);
            return "redirect:/admin";
        } catch (Exception e) {
            model.addAttribute("message", "User update failed");
            return "/admin/edit";
        }
    }

    @PostMapping("delete")
    public String deleteUser(@RequestParam("id") Long id) {
        userRepository.deleteById(id);
        return "redirect:/admin";
    }
}
