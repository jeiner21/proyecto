package com.project.java.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Table(name = "user_db")
@Data
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "El nombre es obligatorio")
    private String username;

    @Column(nullable = false)
    @NotBlank(message = "El correo electronico es obligatorio")
    @Email(message = "Debe ser un correo electronico valido")
    private String email;

    @Column(nullable = false)
    @NotBlank(message = "La contraseña es obigatoria")
    @Size(min = 8, message = "La contraseña minimo debe tener 8 caracteres")
    private String password;

}
