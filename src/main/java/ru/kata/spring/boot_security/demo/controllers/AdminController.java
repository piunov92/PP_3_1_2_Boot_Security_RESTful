package ru.kata.spring.boot_security.demo.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.Role;
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
        return "admin/index";
    }

//    @GetMapping("new")
//    public String registerUser(@ModelAttribute("user") User user, Model model) {
//        model.addAttribute("allRoles", roleService.getAllRoles());
//        return "/admin/new";
//    }

    @PostMapping("new")
    public String saveUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult,  @RequestParam List<String> roleNames, Model model) {
        if (bindingResult.hasErrors()) {
            return "admin/new";
        }
        try {
            userService.registerUser(user, roleNames);
            return "redirect:/admin";
        } catch (Exception e) {
            model.addAttribute("error", "Error when filling the form, please try again");
            return "admin/new";
        }
    }

    @GetMapping("edit")
    public String editUser(@RequestParam("id") Long id, Model model) {
        User user = userService.findUserById(id);
        List<Role> allRoles = roleService.getAllRoles();

        model.addAttribute("user", user);
        model.addAttribute("allRoles", allRoles);
        return "admin/edit";
    }

    @PostMapping("edit")
    public String updateUser(@ModelAttribute User user, @RequestParam List<String> roleNames, Model model) {
        try {
            userService.updateUser(user, roleNames);
            return "redirect:/admin";
        } catch (Exception e) {
            model.addAttribute("message", "User update failed");
            return "/admin/edit";
        }
    }

    @PostMapping("delete")
    public String deleteUser(@RequestParam("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }
}