package com.UFMSPetSistemas.getpet.model.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Categoria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O título deve ser informado")
    private String titulo;

    public Categoria(String titulo) {
        this.titulo = titulo;
    }

    public Categoria() {
    }

    // GETTERS
    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    // SETTERS
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
}
