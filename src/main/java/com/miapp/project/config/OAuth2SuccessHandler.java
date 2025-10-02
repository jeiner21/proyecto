package com.miapp.project.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.miapp.project.model.User;
import com.miapp.project.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;
    private final UserService userService;

    public OAuth2SuccessHandler(JwtUtil jwtUtil, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");

         Optional<User> existingUser = userService.searchUser(email);
        if (existingUser.isEmpty()) {
            User newUser = new User();
            newUser.setUsername(email);
            newUser.setPassword(""); // o generar un password random
            userService.save(newUser);
        }
        // Generar JWT usando tu JwtUtil
        String token = jwtUtil.generateToken(email);

        // Redirigir al cliente con el JWT (puedes cambiar la URL según tu front)
        response.sendRedirect("http://localhost:3000/home?token=" + token);
    }
}
