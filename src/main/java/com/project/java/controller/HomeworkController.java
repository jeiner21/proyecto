package com.project.java.controller;

import com.project.java.model.Homework;
import com.project.java.service.HomeworkService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/homeworks")
public class HomeworkController {

    private final HomeworkService homeworkService;

    public HomeworkController(HomeworkService homeworkService) {
        this.homeworkService = homeworkService;
    }

    // Obtener todas las tareas del usuario autenticado
    @GetMapping
    public ResponseEntity<List<Homework>> getAllHomeworks() {
        List<Homework> homeworks = homeworkService.getAllHomeworks();
        return ResponseEntity.ok(homeworks);
    }

    // Crear nueva tarea
    @PostMapping
    public ResponseEntity<Homework> createHomework(@Valid @RequestBody Homework homework) {
        Homework savedHomework = homeworkService.createHomework(homework);
        return ResponseEntity.ok(savedHomework);
    }

    // Obtener una tarea específica
    @GetMapping("/{id}")
    public ResponseEntity<Homework> getHomeworkById(@PathVariable Long id) {
        return homeworkService.getHomeworkById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Actualizar tarea
    @PutMapping("/{id}")
    public ResponseEntity<Homework> updateHomework(@PathVariable Long id,
            @Valid @RequestBody Homework updatedHomework) {
        try {
            Homework homework = homeworkService.updateHomework(id, updatedHomework);
            return ResponseEntity.ok(homework);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Eliminar tarea
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHomework(@PathVariable Long id) {
        try {
            homeworkService.deleteHomework(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
