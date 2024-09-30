package ru.kata.spring.boot_security.demo.services;

import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.models.UserForm;

import java.util.List;

public interface UserService {
    void newUser(UserForm userForm);
    void updateUser(UserForm userForm, Long id);
    User findUserById(Long id);
    Long getUserIdByUsername(String username);
}
