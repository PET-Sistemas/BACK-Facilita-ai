package com.UFMSPetSistemas.getpet.controller.autenticacao;

import com.UFMSPetSistemas.getpet.controller.autenticacao.dto.AutenticacaoDTO;
import com.UFMSPetSistemas.getpet.controller.autenticacao.dto.RegistroDTO;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.ResponseEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Auth", description = "Endpoints para gerenciamento de login e registro.")
public interface IntAutenticacaoController {

    @PostMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )

    @Operation(
            operationId = "cadastrarUsuario",
            summary = "Cadastrar um novo usuário.",
            description = "Recebe os dados de um novo usuário e o salva no banco de dados.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados do novo usuário",
                    required = true,
                    content = @Content(
                            mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                            schema = @Schema(implementation = RegistroDTO.class),
                            examples = @ExampleObject(
                                    name = "Exemplo de usuário",
                                    value = "{\n" +
                                            "  \"email\": \"gabriel@email.com\",\n" +
                                            "  \"senha\": \"senha123\",\n" +
                                            "  \"role\": \"USER\",\n" +
                                            "  \"nomeCompleto\": \"Gabriel da Silva\",\n" +
                                            "  \"dataNascimento\": \"2000-01-01\",\n" +
                                            "  \"endereco\": \"Rua ufms, 1\",\n" +
                                            "  \"cidade\": \"Campo Grande\",\n" +
                                            "  \"uf\": \"MS\",\n" +
                                            "  \"telefone\": \"67999998888\"\n" +
                                            "}"
                            )
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "201",
                            description = "Usuário cadastrado com sucesso.",
                            content = @Content(
                                    examples = {@ExampleObject(
                                            name = "Novo Usuário Exemplo João",
                                            summary = "",
                                            description = "Usuário cadastrado com todos os campos preenchidos, mas serviços em null.",
                                            value = "{" +
                                                    "  \"id\": 7,\n" +
                                                    "  \"email\": \"gabriel@email.com\",\n" +
                                                    "  \"senha\": \"senha123\",\n" +
                                                    "  \"role\": \"USER\",\n" +
                                                    "  \"nomeCompleto\": \"Gabriel da Silva\",\n" +
                                                    "  \"dataNascimento\": \"2000-01-01\",\n" +
                                                    "  \"endereco\": \"Rua ufms, 1\",\n" +
                                                    "  \"cidade\": \"Campo Grande\",\n" +
                                                    "  \"uf\": \"MS\",\n" +
                                                    "  \"telefone\": \"67999998888\"\n" +
                                                    "}")
                                    },
                                    mediaType = MediaType.APPLICATION_JSON_VALUE
                            )
                    ),
                    @ApiResponse(responseCode = "400",
                            description = "E-mail já cadastrado ou JSON inválido.",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @ExampleObject(
                                                    name = "Erro de cadastro",
                                                    summary = "E-mail duplicado",
                                                    description = "Retorna erro quando o e-mail informado já está cadastrado.",
                                                    value = "{\n" +
                                                            "  \"mensagem\": \"E-mail já cadastrado\",\n" +
                                                            "  \"status\": 400\n" +
                                                            "}"
                                            )
                                    }
                            )),
            }
    )
    @ResponseBody
    ResponseEntity<?> register(
            @Parameter(description = "Dados do usuário (form-data)", required = true)
            @ModelAttribute RegistroDTO data, // Use ModelAttribute para form-data

            @Parameter(description = "Arquivo de foto de perfil (.jpg, .png)")
            @RequestPart(value = "fotoPerfil", required = false) MultipartFile foto
    );
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )

    @Operation(
            operationId = "logar",
            summary = "Efetuar login e gerar token de autenticação.",
            description = "Recebe os dados de email e senha do usuário e confere com os salvos no banco de dados.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados de login",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Exemplo de login",
                                    summary = "JSON válido para efetuar login",
                                    value = "{\n" +
                                            "  \"email\": \"gabriel@email.com\",\n" +
                                            "  \"senha\": \"senha123\"\n" +
                                            "}"
                            )
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Usuário autenticado com sucesso.", content = @Content(
                            examples = {@ExampleObject(
                                    name = "Exemplo login",
                                    summary = "",
                                    description = "Usuário autenticado.",
                                    value = "{" +
                                            " \"token\": \"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJhdXRoLWFwaSIsInN1YiI6ImdhYnJpZWxAZW1haWwuY29tIiwiZXhwIjoxNzYxMjc4NDIzfQ.gFMZKX87X7KgcIt4V_v2VOSxWSB0r-F4bpY4IAKopNI\"" +
                                            "}")
                            },
                            mediaType = MediaType.APPLICATION_JSON_VALUE
                    )
                    ),
                    @ApiResponse(responseCode = "401",
                            description = "Credenciais inválidas.",
                            content =
                            @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    examples = {
                                            @ExampleObject(
                                                    name = "Erro de autenticação",
                                                    summary = "Email ou senha incorretos",
                                                    description = "Retorna uma mensagem de erro quando o login falha.",
                                                    value = "{\n" +
                                                            "  \"mensagem\": \"Email ou senha inválidos\",\n" +
                                                            "  \"status\": 401\n" +
                                                            "}"
                                            )}))
            }
    )

    @ResponseBody
    ResponseEntity login(@RequestBody @Valid AutenticacaoDTO data);
}
