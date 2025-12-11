package com.UFMSPetSistemas.getpet.services;

import com.UFMSPetSistemas.getpet.model.entities.PasswordResetToken;
import com.UFMSPetSistemas.getpet.model.entities.Usuario;
import com.UFMSPetSistemas.getpet.model.repository.PasswordResetTokenRepository;
import com.UFMSPetSistemas.getpet.model.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Date;


import java.util.UUID;

@Service
public class PasswordResetService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired private PasswordResetTokenRepository tokenRepository;
    @Autowired private JavaMailSender mailSender;
    @Autowired private PasswordEncoder passwordEncoder;

    public void solicitarRecuperacao(String email) {
        var usuarioEncontrado = usuarioRepository.findByEmail(email);

        if (usuarioEncontrado == null) {
            throw new EntityNotFoundException("Email não encontrado");
        }

        Usuario usuario = (Usuario) usuarioEncontrado;

        String tokenString = UUID.randomUUID().toString();

        PasswordResetToken tokenEntity = tokenRepository.findByUsuario(usuario)
                .orElse(new PasswordResetToken()); // Se não tiver, cria um objeto vazio

        tokenEntity.setUsuario(usuario);
        tokenEntity.setToken(tokenString);
        tokenEntity.setExpiryDate(new Date(System.currentTimeMillis() + (60 * 60 * 1000)));

        tokenRepository.save(tokenEntity);

        enviarEmail(usuario.getEmail(), tokenString);
    }

    @Transactional
    public void trocarSenha(String token, String novaSenha) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Token inválido"));

        if (resetToken.isExpirado()) {
            tokenRepository.delete(resetToken);
            throw new IllegalArgumentException("Token expirado");
        }

        Usuario usuario = resetToken.getUsuario();
        usuario.setSenha(passwordEncoder.encode(novaSenha));
        usuarioRepository.save(usuario);

        tokenRepository.delete(resetToken);
    }

    private void enviarEmail(String para, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(para);
        message.setSubject("Recuperação de Senha");
        message.setText("Para redefinir sua senha, clique no link: "
                + "http://localhost:8081/resetar-senha?token=" + token);

        mailSender.send(message);
    }
}
