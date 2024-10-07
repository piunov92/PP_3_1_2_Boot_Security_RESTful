package ru.kata.spring.boot_security.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

@Getter
@Setter
@AllArgsConstructor
public class UserDto {
    private String username;
    private String password;
    private String email;
    private Collection<String> roles;

    public UserDto() {}
}
