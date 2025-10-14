package com.project.java.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import com.project.java.model.Homework;
import com.project.java.model.Users;
import com.project.java.repository.HomeworkRepository;
import com.project.java.repository.Repository;

@Service
public class HomeworkService {
    private final HomeworkRepository homeworkRepository;
    private final Repository repository;

    public HomeworkService(HomeworkRepository homeworkRepository, Repository repository) {
        this.homeworkRepository = homeworkRepository;
        this.repository = repository;
    }

    // obetner user
    private String getAuthenticatedEmail() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            return principal.toString();
        }
    }

    // listar tareas de user
    public List<Homework> getAllHomeworks() {
        String email = getAuthenticatedEmail();
        return homeworkRepository.findByUsers_Email(email);
    }

    // crear tarea
    public Homework createHomework(Homework homework) {
        String email = getAuthenticatedEmail();
        Users user = repository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        homework.setUsers(user);
        return homeworkRepository.save(homework);
    }

    // obtener tareas por id
    public Optional<Homework> getHomeworkById(Long id) {
        String email = getAuthenticatedEmail();
        return homeworkRepository.findById(id)
                .filter(hw -> hw.getUsers().getEmail().equals(email));
    }

    // Actualizar tareas
    public Homework updateHomework(Long id, Homework newHomework) {
        return getHomeworkById(id)
                .map(hw -> {
                    hw.setTitle(newHomework.getTitle());
                    hw.setDescription(newHomework.getDescription());
                    hw.setStatus(newHomework.getStatus());
                    hw.setDate(newHomework.getDate());
                    return homeworkRepository.save(hw);
                })
                .orElseThrow(() -> new RuntimeException("La tareas no se encontro"));
    }

    // eliminar tareas
    public void deleteHomework(Long id) {
        getHomeworkById(id).ifPresent(homeworkRepository::delete);
    }

}
