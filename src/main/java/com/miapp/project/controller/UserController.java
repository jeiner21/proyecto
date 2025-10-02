package com.miapp.project.controller;

import com.miapp.project.config.JwtUtil;
import com.miapp.project.model.User;
import com.miapp.project.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserController(UserService userService, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    // Registro de usuario
    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        User savedUser = userService.save(user);
        return ResponseEntity.ok(savedUser);

    }

    // Login
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User loginData) {
        Optional<User> user = userService.searchUser(loginData.getUsername());

        if (user.isPresent() && passwordEncoder.matches(loginData.getPassword(), user.get().getPassword())) {
            String token = jwtUtil.generateToken(user.get().getUsername());
            return ResponseEntity.ok("✅ Login exitoso" + "token " + token);
        } else {
            return ResponseEntity.status(401).body("❌ Usuario o contraseña incorrectos");
        }
    }
}
