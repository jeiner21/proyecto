package com.project.java.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.java.model.Homework;

public interface HomeworkRepository extends JpaRepository<Homework, Long> {
    List<Homework> findByUsers_Email(String email);

}
