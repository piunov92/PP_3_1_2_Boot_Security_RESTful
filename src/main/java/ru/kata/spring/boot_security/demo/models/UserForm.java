package ru.kata.spring.boot_security.demo.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserForm {

    @NotEmpty(message = "The field must not be empty")
    @Size(min = 2, max = 10, message = "invalid value, 2 to 10 characters allowed")
    private String username;

    @NotEmpty(message = "The field must not be empty")
    private String password;

    @NotEmpty(message = "The field must not be empty")
    @Email
    private String email;

    @NotEmpty(message = "The field must not be empty")
    private String roles;
}
