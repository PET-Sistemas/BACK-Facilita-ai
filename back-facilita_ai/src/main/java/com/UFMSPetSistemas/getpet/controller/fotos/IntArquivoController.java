package com.UFMSPetSistemas.getpet.controller.fotos;

import com.UFMSPetSistemas.getpet.model.entities.Usuario;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Arquivos", description = "Gerenciamento de upload e download de fotos de perfil.")
@RequestMapping("/files")
public interface IntArquivoController {

    @Operation(
            summary = "Upload da foto de perfil",
            description = "Envia uma nova foto de perfil para o usuário logado e remove a anterior (se houver).",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Upload realizado com sucesso",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UploadResponseDTO.class))
                    ),
                    @ApiResponse(responseCode = "403", description = "Usuário não autenticado"),
                    @ApiResponse(responseCode = "500", description = "Erro interno no upload")
            }
    )
    @PostMapping(value = "/upload/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<UploadResponseDTO> uploadProfile(
            @Parameter(hidden = true) // Esconde do Swagger (vem do Token)
            Usuario usuario,

            @Parameter(description = "Arquivo de imagem (.png, .jpg, .jpeg)", required = true)
            @RequestPart("file") MultipartFile file
    );

    @Operation(
            summary = "Visualizar foto de perfil",
            description = "Retorna os bytes da imagem de perfil do usuário logado para visualização direta.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Imagem recuperada com sucesso",
                            content = @Content(mediaType = "image/jpeg") // Indica que retorna uma imagem
                    ),
                    @ApiResponse(responseCode = "404", description = "Usuário não possui foto de perfil"),
                    @ApiResponse(responseCode = "403", description = "Usuário não autenticado")
            }
    )
    @GetMapping("/view/profile")
    ResponseEntity<byte[]> viewProfile(
            @Parameter(hidden = true) // Esconde do Swagger
            Usuario usuario
    );
}