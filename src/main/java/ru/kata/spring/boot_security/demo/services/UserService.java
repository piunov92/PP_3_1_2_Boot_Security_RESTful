package ru.kata.spring.boot_security.demo.services;

import ru.kata.spring.boot_security.demo.dto.UserDto;
import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();
    User createUser(UserDto userDto);
    void updateUser(Long id, UserDto userDto);
    void deleteUser(Long id);
    User findUserById(Long id);
    Long getUserIdByUsername(String username);
}
