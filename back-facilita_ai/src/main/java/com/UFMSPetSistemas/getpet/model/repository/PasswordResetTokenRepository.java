package com.UFMSPetSistemas.getpet.model.repository;

import com.UFMSPetSistemas.getpet.model.entities.PasswordResetToken;
import com.UFMSPetSistemas.getpet.model.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(String token);
    Optional<PasswordResetToken> findByUsuario(Usuario usuario);
}
