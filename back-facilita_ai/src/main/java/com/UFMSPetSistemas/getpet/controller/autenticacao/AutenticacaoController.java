package com.UFMSPetSistemas.getpet.controller.autenticacao;


import com.UFMSPetSistemas.getpet.controller.autenticacao.dto.AutenticacaoDTO;
import com.UFMSPetSistemas.getpet.controller.autenticacao.dto.LoginResponseDTO;
import com.UFMSPetSistemas.getpet.controller.autenticacao.dto.RegistroDTO;
import com.UFMSPetSistemas.getpet.model.entities.Usuario;
import com.UFMSPetSistemas.getpet.model.repository.UsuarioRepository;
import com.UFMSPetSistemas.getpet.security.TokenService;
import com.UFMSPetSistemas.getpet.services.MinioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;


@RestController
@RequestMapping("auth")
public class AutenticacaoController implements IntAutenticacaoController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MinioService minioService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AutenticacaoDTO data) {
        // cria uma autenticação com usuario e senha
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.senha());
        // verifica se usuario e senha estão corretos utilizando o authenticationManager da classe securityConfig
        var auth = this.authenticationManager.authenticate(usernamePassword);
        // cria um token se usuário e senha estiverem corretos
        var token = tokenService.generateToken((Usuario) auth.getPrincipal());
        //retorna o token pelo DTO
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity register(
            @Valid @ModelAttribute RegistroDTO data,
            @RequestPart(value = "fotoPerfil", required = false) MultipartFile fotoPerfil
    ) {
        if (this.repository.findByEmail(data.email()) != null) return ResponseEntity.badRequest().build();

        String encryptedPassword = this.passwordEncoder.encode(data.senha());

        Usuario novoUsuario = new Usuario(data.email(), encryptedPassword, data.role(), data.nomeCompleto(), data.dataNascimento(), data.endereco(), data.cidade(), data.uf(), data.telefone(), null);

        this.repository.save(novoUsuario);

        if (fotoPerfil != null && !fotoPerfil.isEmpty()) {
            String objectName = "usuarios/perfil/" + novoUsuario.getId().toString() + UUID.randomUUID();
            minioService.upload(fotoPerfil, objectName);
            novoUsuario.setFotoPerfil(minioService.generatePresignedUrl(objectName));
            this.repository.save(novoUsuario);
        }

        return ResponseEntity.ok().build();
    }
}
