package com.project.java.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.java.model.Users;

public interface Repository extends JpaRepository<Users, Long> {
    Optional<Users> findByEmail(String email);

}
