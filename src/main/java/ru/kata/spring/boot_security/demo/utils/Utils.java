package ru.kata.spring.boot_security.demo.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import ru.kata.spring.boot_security.demo.models.User;

public class Utils {
    public static void auth(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = null;

        if (authentication != null && authentication.getPrincipal() instanceof User) {
            user = (User) authentication.getPrincipal();

        }

        if (user != null) {
            model.addAttribute("_id", user.getId());
            model.addAttribute("_username", user.getUsername());
            model.addAttribute("_password", user.getPassword());
            model.addAttribute("_email", user.getEmail());
            model.addAttribute("_roles", user.getAuthorities());
        }
    }
}
