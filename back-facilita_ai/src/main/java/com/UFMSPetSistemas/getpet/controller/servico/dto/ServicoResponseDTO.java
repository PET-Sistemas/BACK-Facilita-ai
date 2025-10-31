package com.UFMSPetSistemas.getpet.controller.servico.dto;

// Este record define um objeto de transferência de dados que carregará
// apenas os campos que queremos expor na resposta da API.
public record ServicoResponseDTO(String titulo, String descricao, double valor, String categoriaNome) {
}
