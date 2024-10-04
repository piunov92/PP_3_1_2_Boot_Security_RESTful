package ru.kata.spring.boot_security.demo.services;

import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();
    void registerUser(User user, List<String> roleNames);
    void updateUser(Long id, User user, List<String> roleNames) throws Exception;
    void deleteUser(Long id);
    User findUserById(Long id);
    Long getUserIdByUsername(String username);
}
