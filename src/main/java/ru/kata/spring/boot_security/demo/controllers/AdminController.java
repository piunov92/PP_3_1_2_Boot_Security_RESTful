package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.models.UserForm;
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
    public String Users(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        model.addAttribute("userForm", new UserForm());
        model.addAttribute("error");
        model.addAttribute("username", userDetails.getUsername());
        model.addAttribute("roles", userDetails.getAuthorities());
        model.addAttribute("users", userRepository.findAll());
        return "/admin/index";
    }

    @PostMapping("submit")
    @ResponseBody
    public ResponseEntity<String> addUser(@ModelAttribute("userForm") UserForm userForm, Model model) {
        try {
            userService.newUser(userForm);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalStateException e) {
            model.addAttribute("error", "error" );
            return new ResponseEntity<>("User already exists", HttpStatus.CONFLICT);
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
            userService.updateUser(userForm, id);
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
