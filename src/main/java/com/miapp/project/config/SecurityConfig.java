package com.miapp.project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.miapp.project.service.UserService;

@Configuration
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    public SecurityConfig(JwtFilter jwtFilter, JwtUtil jwtUtil, UserService userService) {
        this.jwtFilter = jwtFilter;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public OAuth2SuccessHandler oAuth2SuccessHandler(JwtUtil jwtUtil, UserService userService) {
        return new OAuth2SuccessHandler(jwtUtil, userService);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Desactivar CSRF para pruebas con Postman
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/register", "/auth/login", "/oauth2/**").permitAll() // permitir estos
                                                                                                    // endpoints
                        .anyRequest().authenticated() // todo lo demás requiere autenticación
                )
                .oauth2Login(oauth -> oauth
                        .successHandler(oAuth2SuccessHandler(jwtUtil, userService)))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
