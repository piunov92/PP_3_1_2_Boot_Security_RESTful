package ru.kata.spring.boot_security.demo.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.models.UserForm;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Long getUserIdByUsername(String username) {
        User user = userRepository.findByUsername(username);
        return (user != null) ? user.getId() : null;
    }

    @Override
    public User findUserById(Long id) {
        Optional<User> userOptional = userRepository.findById(id);

        if (userOptional.isPresent()) {
            return userOptional.get();
        } else {
            throw new RuntimeException("User not found.");
        }
    }

    @Override
    @Transactional
    public void newUser(UserForm userForm) {
        if (userRepository.findByUsername(userForm.getUsername()) != null) {
            throw new IllegalStateException("User already exists");
        }
        Set<Role> roles = new HashSet<>();
        for (String roleName : userForm.getRoles()) {
            Role role = roleRepository.findByName(roleName);
            if (role == null) {
                role = new Role(roleName);
                roleRepository.save(role);
            }
            roles.add(role);
        }
        String encodedPassword = passwordEncoder.encode(userForm.getPassword());
        userRepository.save(new User(userForm.getUsername(), encodedPassword, userForm.getEmail(), roles));
    }

    @Override
    @Transactional
    public void updateUser(UserForm userForm, Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalStateException("User not found."));
        user.setUsername(userForm.getUsername());
        user.setEmail(userForm.getEmail());

        if (userForm.getPassword() != null && !userForm.getPassword().isEmpty()) {
            String encodedPassword = passwordEncoder.encode(userForm.getPassword());
            user.setPassword(encodedPassword);
        }

        Set<Role> roles = new HashSet<>();
        for (String roleName : userForm.getRoles()) {
            Role role = roleRepository.findByName(roleName);
            if (role != null) {
                roles.add(role);
            } else {
                role = new Role(roleName);
                roleRepository.save(role);
                roles.add(role);
            }
        }
        user.setRoles(roles);
        userRepository.save(user);
    }
}
