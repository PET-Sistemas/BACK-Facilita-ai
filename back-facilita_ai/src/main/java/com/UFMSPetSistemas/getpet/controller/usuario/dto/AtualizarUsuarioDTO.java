package com.UFMSPetSistemas.getpet.controller.usuario.dto;

import com.UFMSPetSistemas.getpet.model.entities.UserRole;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

import java.util.Date;

public record AtualizarUsuarioDTO(String email,

                                  String senha,

                                  UserRole role,

                                  @JsonProperty("nomeCompleto") String nomeCompleto,

                                  @JsonProperty("dataNascimento") Date dataNascimento,

                                  @JsonProperty("endereco") String endereco,

                                  @JsonProperty("cidade") String cidade,

                                  @JsonProperty("uf") String uf,

                                  @JsonProperty("telefone") String telefone
) {
}
