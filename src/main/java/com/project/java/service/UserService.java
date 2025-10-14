package com.project.java.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.project.java.model.Users;
import com.project.java.repository.Repository;

@Service
public class UserService {
    private final Repository repository;
    private final PasswordEncoder passwordEncoder;

    public UserService(Repository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<Users> searchUser(String email) {
        return repository.findByEmail(email);
    }

    public Users save(Users users) {
        users.setPassword(passwordEncoder.encode(users.getPassword()));
        return repository.save(users);
    }

}
