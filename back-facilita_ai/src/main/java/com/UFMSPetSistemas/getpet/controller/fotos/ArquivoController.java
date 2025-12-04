package com.UFMSPetSistemas.getpet.controller.fotos;

import com.UFMSPetSistemas.getpet.model.entities.Usuario;
import com.UFMSPetSistemas.getpet.model.repository.UsuarioRepository;
import com.UFMSPetSistemas.getpet.services.MinioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
        try {

            try {
                if (usuario.getFotoPerfil() != null) {
                    String oldObjectName = minioService.extractObjectName(usuario.getFotoPerfil());
                    if (oldObjectName != null) {
                        minioService.delete(usuario.getFotoPerfil());
                    }
                }
            } catch (Exception e) {
                System.err.println("Erro: " + e.getMessage());
            }

            String objectName = "usuarios/perfil/" + usuario.getId() + UUID.randomUUID();
            minioService.upload(file, objectName);

            String url = objectName;
            usuario.setFotoPerfil(url);
            usuarioRepo.save(usuario);

            return ResponseEntity.ok(new UploadResponseDTO(url));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/view/profile")
    public ResponseEntity<byte[]> viewProfile(@AuthenticationPrincipal Usuario usuario) {
        try {
            if (usuario.getFotoPerfil() == null) {
                return ResponseEntity.notFound().build();
            }
            String objectName = minioService.extractObjectName(usuario.getFotoPerfil());

            if (objectName == null) {
                System.err.println("URL inválida ou corrompida, não foi possível extrair nome do arquivo.");
                return ResponseEntity.notFound().build();
            }

            byte[] bytes = minioService.getObject(objectName);
            String contentType = minioService.getContentType(objectName);

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(bytes);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
}
