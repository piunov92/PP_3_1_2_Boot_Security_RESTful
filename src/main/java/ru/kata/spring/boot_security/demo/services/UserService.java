package ru.kata.spring.boot_security.demo.services;

import ru.kata.spring.boot_security.demo.models.User;
import java.util.List;

public interface UserService {
    void newUser(String username, String password, String email, List<String> roles);
    User findUserById(Long id);
    Long getUserIdByUsername(String username);
}
