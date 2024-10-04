package ru.kata.spring.boot_security.demo.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import java.util.*;

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
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public void registerUser(User user, List<String> roleNames) {
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new IllegalStateException("User already exists");
        }
        Set<Role> roles = new HashSet<>();
        for (String roleName : roleNames) {
            Role role = roleRepository.findByName(roleName).orElse(null);
            if (role == null) {
                role = new Role(roleName);
                roleRepository.save(role);
            }
            roles.add(role);
        }
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        userRepository.save(new User(user.getUsername(), encodedPassword, user.getEmail(), roles));
    }

    @Override
    @Transactional
    public void updateUser(Long id, User user, List<String> roleNames) throws Exception {
        User foundUser = userRepository.findById(id).orElseThrow(() -> new IllegalStateException("User not found."));

        if (!foundUser.getUsername().equals(user.getUsername())) {
            Optional<User> userWithSameUsername = Optional.ofNullable(userRepository.findByUsername(user.getUsername()));
            if (userWithSameUsername.isPresent()) {
                throw new Exception("Username " + user.getUsername() + " is already taken");
            }
        }
        foundUser.setUsername(user.getUsername());
        foundUser.setEmail(user.getEmail());

        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            foundUser.setPassword(encodedPassword);
        }

        Set<Role> roles = new HashSet<>();
        for (String roleName : roleNames) {
            Role role = roleRepository.findByName(roleName).orElse(null);
            if (role != null) {
                roles.add(role);
            } else {
                role = new Role(roleName);
                roleRepository.save(role);
                roles.add(role);
            }
        }
        foundUser.setRoles(roles);
        userRepository.save(foundUser);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
