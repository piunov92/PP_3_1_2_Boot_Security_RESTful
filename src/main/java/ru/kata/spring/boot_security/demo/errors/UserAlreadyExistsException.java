package ru.kata.spring.boot_security.demo.errors;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String username) {
        super("User already exists " + username);
    }
}
