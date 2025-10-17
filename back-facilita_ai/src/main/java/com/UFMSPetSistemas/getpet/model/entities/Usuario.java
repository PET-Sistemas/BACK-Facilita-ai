package com.UFMSPetSistemas.getpet.model.entities;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import jakarta.validation.constraints.Pattern;
import jakarta.persistence.*;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
public class Usuario implements UserDetails {
    /* ATRIBUTOS */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomeCompleto;

    @Temporal(TemporalType.DATE)
    private Date dataNascimento;

    private String endereco;

    private String cidade;

    private String uf;

    private String email;

    @Pattern(regexp = "\\d{11}", message = "O telefone deve ter 11 dígitos numéricos.")
    private String telefone;

    private String senha;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    /* CONSTRUTORES */
    public Usuario(String nomeCompleto,
                   Date dataNascimento,
                   String endereco,
                   String cidade,
                   String uf,
                   String email,
                   String telefone,
                   String senha
    ) {
        this.nomeCompleto = nomeCompleto;
        this.dataNascimento = dataNascimento;
        this.endereco = endereco;
        this.cidade = cidade;
        this.uf = uf;
        this.email = email;
        this.telefone = telefone;
        this.senha = senha;
    }

    public Usuario(String email,
                   String senha,
                   UserRole role,
                   String nomeCompleto,
                   Date dataNascimento,
                   String endereco,
                   String cidade,
                   String uf,
                   String telefone) {
        this.email = email;
        this.senha = senha;
        this.role = role;
        this.nomeCompleto = nomeCompleto;
        this.dataNascimento = dataNascimento;
        this.endereco = endereco;
        this.cidade = cidade;
        this.uf = uf;
        this.telefone = telefone;

    }

    public Usuario() {
    } // Construtor sem argumentos para o framework

    /**
     * Factory Method para criar novo Usuario quando construtor for privado
     *
     */
    public static Usuario newUsuario() {
        System.out.println("Não implementado");

        return new Usuario();
    }

    /* GETTERS */
    public Long getId() {
        return id;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public String getEndereco() {
        return endereco;
    }

    public String getCidade() {
        return cidade;
    }

    public String getUf() {
        return uf;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefone() {
        return telefone;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public String getSenha() {
        return senha;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    /* MÉTODOS DA CLASSE */

    public Usuario update(
            String nomeCompleto,
            Date dataNascimento,
            String endereco,
            String cidade,
            String uf,
            String email,
            String telefone,
            String senha
    ) {
        setNomeCompleto(nomeCompleto);
        setDataNascimento(dataNascimento);
        setEndereco(endereco);
        setCidade(cidade);
        setUf(uf);
        setEmail(email);
        setTelefone(telefone);
        setSenha(senha);

        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(id, usuario.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (this.role == UserRole.ADMIN)
            return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER"));
        else
            return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}