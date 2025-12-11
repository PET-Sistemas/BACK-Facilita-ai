package com.UFMSPetSistemas.getpet.controller.servico.dto;

public record ServicoDetalhadoDTO(
        Long id,
        String titulo,
        String descricao,
        double valor,
        PrestadorDTO prestador,
        double mediaAvaliacoes,
        int totalAvaliacoes) {
}
