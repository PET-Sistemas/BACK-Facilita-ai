package com.UFMSPetSistemas.getpet.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Servico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;

    private String descricao;

    private double valor;

    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    @ManyToOne
    @JoinColumn(name = "usuario_prestador_id")
    private Usuario usuarioPrestador;

    @OneToMany(mappedBy = "servico")
    @JsonIgnore
    private List<PrestacaoServico> prestacoes;

    public Servico(
                   String titulo,
                   String descricao,
                   double valor,
                   Categoria categoria,
                   Usuario usuarioPrestador
    ) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.valor = valor;
        this.categoria = categoria;
        this.usuarioPrestador = usuarioPrestador;
    }

    public Servico(){}

    // GETTERS

    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public double getValor() {
        return valor;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public Usuario getUsuarioPrestador() {
        return usuarioPrestador;
    }

    // SETTERS

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public void setUsuarioPrestador(Usuario usuarioPrestador) {
        this.usuarioPrestador = usuarioPrestador;
    }

    public Double getMediaAvaliacoes() {
        if (prestacoes == null || prestacoes.isEmpty()) {
            return 0.0;
        }
        double soma = 0.0;
        int contador = 0;

        for (PrestacaoServico p : prestacoes) {
            if (p.getAvaliacao() != null) {
                soma += p.getAvaliacao();
                contador++;
            }
        }

        if (contador == 0) return 0.0;

        return soma / contador;
    }

    public Integer getTotalAvaliacoes() {
        if (prestacoes == null) return 0;

        return (int) prestacoes.stream()
                .filter(p -> p.getAvaliacao() != null)
                .count();
    }
}

