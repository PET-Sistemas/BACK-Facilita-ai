package com.UFMSPetSistemas.getpet.controller.autenticacao.dto;

import com.UFMSPetSistemas.getpet.model.entities.UserRole;

public record RegistroDTO(String login, String senha, UserRole role) {
}
