package com.UFMSPetSistemas.getpet.controller.servico.dto;

public record PrestadorDTO(
        String nome,
        String enderecoCompleto,
        String telefoneWhatsapp,
        double mediaAvaliacoes,
        int totalAvaliacoes) {
}
