package com.UFMSPetSistemas.getpet.model.entities;

import jakarta.persistence.*;
import java.util.Date;

@Entity
public class PasswordResetToken {

    public Long getId() {
        return id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String token;

    @OneToOne(targetEntity = Usuario.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "usuario_id")
    private Usuario usuario;

    private Date expiryDate;

    public PasswordResetToken(String token, Usuario usuario) {
        this.token = token;
        this.usuario = usuario;
        // Expira em 1 hora (60 min * 60 seg * 1000 ms)
        this.expiryDate = new Date(System.currentTimeMillis() + (60 * 60 * 1000));
    }

    public PasswordResetToken() {}

    public boolean isExpirado() {
        return new Date().after(this.expiryDate);
    }

    public String getToken() { return token; }
    public Usuario getUsuario() { return usuario; }

    public void setToken(String token) {
        this.token = token;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
