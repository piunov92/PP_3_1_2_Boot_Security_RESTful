package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.dto.UserDto;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;
import ru.kata.spring.boot_security.demo.utils.Utils;
import java.util.List;

@Controller
public class UserController {
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public UserController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }


    @GetMapping("/user")
    public String user(Model model) {
        Utils.auth(model);
        return "user";
    }

    @GetMapping("/admin")
    public String users(@ModelAttribute("user") User user, Model model) {
        Utils.auth(model);

        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("userDto", new UserDto());
        model.addAttribute("allRoles", roleService.getAllRoles());
        model.addAttribute("error");
        return "admin/index";
    }

//    @PostMapping("/admin/update/{id}")
//    public String updateUser(@PathVariable Long id, @ModelAttribute("user") User user, @RequestParam List<String> roleNames, Model model) {
//        try {
//            userService.updateUser(id, user, roleNames);
//            return "redirect:/admin";
//        } catch (Exception ignored) {
//        }
//        return "redirect:/admin";
//    }
}