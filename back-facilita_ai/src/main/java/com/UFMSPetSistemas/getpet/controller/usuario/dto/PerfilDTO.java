package com.UFMSPetSistemas.getpet.controller.usuario.dto;

import com.UFMSPetSistemas.getpet.model.entities.UserRole;
import com.UFMSPetSistemas.getpet.model.entities.Usuario;

import java.util.Date;

public record PerfilDTO(Long id,
                                String email,
                                UserRole role,
                                String nomeCompleto,
                                Date dataNascimento,
                                String endereco,
                                String cidade,
                                String uf,
                                String telefone,
                                Double mediaAvaliacoes){
    public PerfilDTO(Usuario usuario) {
        this(usuario.getId(),
                usuario.getEmail(),
                usuario.getRole(),
                usuario.getNomeCompleto(),
                usuario.getDataNascimento(),
                usuario.getEndereco(),
                usuario.getCidade(),
                usuario.getUf(),
                usuario.getTelefone(),
                usuario.getMediaGeral()
        );
    }
}
