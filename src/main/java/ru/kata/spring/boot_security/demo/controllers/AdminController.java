package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;
import ru.kata.spring.boot_security.demo.utils.Utils;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public String users(@ModelAttribute("user") User user, Model model) {
        Utils.auth(model);

        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("allRoles", roleService.getAllRoles());
        model.addAttribute("error");
        return "admin/index";
    }

    @PostMapping("register")
    @ResponseBody
    public ResponseEntity<String> saveUser(@ModelAttribute("user") User user, @RequestParam List<String> roleNames, Model model) {
        try {
            userService.registerUser(user, roleNames);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalStateException e) {
            model.addAttribute("error", "error" );
            return new ResponseEntity<>("User already exists", HttpStatus.CONFLICT);
        }
    }

    @PostMapping("update")
    public String updateUser(@ModelAttribute("user") User user, @RequestParam List<String> roleNames, Model model) {
        try {
            userService.updateUser(user, roleNames);
            return "redirect:/admin";
        } catch (Exception ignored) {}
        return "redirect:/admin";
    }

    @PostMapping("delete")
    public String deleteUser(@RequestParam("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }
}