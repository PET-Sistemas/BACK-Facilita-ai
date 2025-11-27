package com.UFMSPetSistemas.getpet.controller.fotos;

import com.UFMSPetSistemas.getpet.model.entities.Usuario;
import com.UFMSPetSistemas.getpet.model.repository.UsuarioRepository;
import com.UFMSPetSistemas.getpet.services.MinioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/files")
public class ArquivoController {

    private final MinioService minioService;
    private final UsuarioRepository usuarioRepo;

    @Autowired
    public ArquivoController(MinioService minioService, UsuarioRepository usuarioRepo) {
        this.minioService = minioService;
        this.usuarioRepo = usuarioRepo;
    }

    @PostMapping(value = "/upload/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UploadResponseDTO> uploadProfile(
            @AuthenticationPrincipal Usuario usuario,
            @RequestPart("file") MultipartFile file
    ) {

        if (usuario.getFotoPerfil() != null) {
            minioService.delete(usuario.getFotoPerfil());
        }

        String objectName = "usuarios/perfil/" + usuario.getId() + UUID.randomUUID();
        minioService.upload(file, objectName);

        String url = minioService.generatePresignedUrl(objectName);
        usuario.setFotoPerfil(url);
        usuarioRepo.save(usuario);

        return ResponseEntity.ok(new UploadResponseDTO(url));
    }

    @GetMapping("/view/profile")
    public ResponseEntity<byte[]> viewProfile(@AuthenticationPrincipal Usuario usuario) {
        try {
            if (usuario.getFotoPerfil() == null) {
                return ResponseEntity.notFound().build();
            }

            String objectName = minioService.extractObjectName(usuario.getFotoPerfil());
            byte[] bytes = minioService.getObject(objectName);
            String contentType = minioService.getContentType(objectName);

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(bytes);

        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}
