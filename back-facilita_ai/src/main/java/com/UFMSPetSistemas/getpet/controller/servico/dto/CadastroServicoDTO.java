package com.UFMSPetSistemas.getpet.controller.servico.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CadastroServicoDTO(
                @NotBlank(message = "O titulo é obrigatório.") @JsonProperty("titulo") String titulo,

                @JsonProperty("descricao") String descricao,

                @NotNull(message = "O valor é obrigatório.") @JsonProperty("valor") Double valor,

                @NotBlank(message = "O nome da categoria é obrigatório.") @JsonProperty("categoriaNome") String categoriaNome,

                @NotNull(message = "O id de usuário prestador é obrigatório.") @JsonProperty("usuarioPrestadorId") Long usuarioPrestadorID) {
}