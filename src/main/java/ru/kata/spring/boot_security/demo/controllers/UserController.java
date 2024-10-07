package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.utils.Utils;

@Controller
public class UserController {
    private final RoleService roleService;

    @Autowired
    public UserController(RoleService roleService) {
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
        model.addAttribute("allRoles", roleService.getAllRoles());
        return "admin/index";
    }
}