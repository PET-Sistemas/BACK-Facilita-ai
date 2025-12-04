package com.UFMSPetSistemas.getpet.controller.prestacaoServico.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import java.time.LocalDate;

@Schema(description = "DTO para registrar uma nova prestação de serviço.")
public record CadastrarPrestacaoServicoDTO(
        @NotNull(message = "A data da prestaçãos é obrigatória.") @PastOrPresent(message = "A data da prestação não pode ser no futuro.") @Schema(description = "Data em que o serviço foi realizado.", example = "2024-07-26", required = true) LocalDate dataPrestacao,

        @Min(value = 1, message = "A avaliação mínima é 1.") @Max(value = 5, message = "A avaliação máxima é 5.") @Schema(description = "Nota de avaliação opcional do serviço (de 1 a 5).", example = "5", required = false) Integer avaliacao,

        @Schema(description = "Comentário opcional sobre o serviço.", example = "Serviço rápido e eficiente.", required = false) String avaliacaodesc,

        @NotNull(message = "O ID do serviço não pode ser nulo.") @Schema(description = "ID do serviço que foi prestado.", example = "15", required = true) Long servicoId ){
}