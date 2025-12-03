package com.UFMSPetSistemas.getpet.controller.autenticacao.dto;

import com.UFMSPetSistemas.getpet.model.entities.UserRole;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public record RegistroDTO(@NotBlank String email,

                          @NotBlank String senha,

                          UserRole role,

                          @NotBlank(message = "O nome completo é obrigatório.")
                          @JsonProperty("nomeCompleto") String nomeCompleto,

                          @DateTimeFormat(pattern = "yyyy-MM-dd")
                          @JsonProperty("dataNascimento") Date dataNascimento,

                          @NotBlank(message = "O endereço é obrigatório.")
                          @JsonProperty("endereco") String endereco,

                          @NotBlank(message = "A cidade é obrigatória.")
                          @JsonProperty("cidade") String cidade,

                          @NotBlank(message = "O estado (UF) é obrigatório.")
                          @JsonProperty("uf") String uf,

                          @NotBlank(message = "O telefone é obrigatório.")
                          @JsonProperty("telefone") String telefone

) {
}
