package com.UFMSPetSistemas.getpet.controller.prestacaoServico.dto;

import java.time.LocalDate;

public record AvaliacaoResponseDTO(
        LocalDate data,
        int numeroEstrelas,
        String descricao,
        String nomeUsuario) {
}
