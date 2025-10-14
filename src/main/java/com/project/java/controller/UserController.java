package com.project.java.controller;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.java.config.JwtUtil;
import com.project.java.model.Users;
import com.project.java.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserController(UserService userService, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<Users> register(@Valid @RequestBody Users users) {
        Users saveUsers = userService.save(users);
        return ResponseEntity.ok(saveUsers);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody Users loginData) {
        Optional<Users> users = userService.searchUser(loginData.getEmail());
        if (users.isPresent() && passwordEncoder.matches(loginData.getPassword(), users.get().getPassword())) {
            String token = jwtUtil.generarToken(users.get().getEmail());
            return ResponseEntity.ok("Login exitoso: token " + token);
        } else {
            return ResponseEntity.status(401).body("Usuario o contraseña incorrectos");
        }

    }

}
