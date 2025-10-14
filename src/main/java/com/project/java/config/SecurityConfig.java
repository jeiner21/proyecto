package com.project.java.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.project.java.service.UserService;

@Configuration
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, UserService userService) throws Exception {
        http
                .csrf(csrf -> csrf.disable())// perimite desactivar para pruebas de postman
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers("/auth/register", "/auth/login").permitAll()// permite el acceso a estas rutas

                        .anyRequest().authenticated()// todo lo demas requiere autenticarse
                );

        return http.build();

    }
}
