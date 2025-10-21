package com.UFMSPetSistemas.getpet.controller.autenticacao;

import com.UFMSPetSistemas.getpet.controller.autenticacao.dto.AutenticacaoDTO;
import com.UFMSPetSistemas.getpet.controller.autenticacao.dto.RegistroDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.UFMSPetSistemas.getpet.controller.usuario.dto.AtualizarUsuarioDTO;
import com.UFMSPetSistemas.getpet.controller.usuario.dto.CadastroUsuarioDTO;
import com.UFMSPetSistemas.getpet.controller.usuario.dto.ListarUsuariosDTO;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.UFMSPetSistemas.getpet.model.entities.Usuario;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Auth", description = "Endpoints para gerenciamento de login e registro.")
public interface IntAutenticacaoController {

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )

    @Operation(
            operationId = "cadastrarUsuario",
            summary = "Cadastrar um novo usuário.",
            description = "Recebe os dados de um novo usuário e o salva no banco de dados.",
            tags = {"Usuário"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados do novo usuário",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Usuario.class),
                            examples = @ExampleObject(
                                    name = "Exemplo de usuário",
                                    summary = "JSON válido para criação de usuário",
                                    value = "{\n" +
                                            "  \"email\": \"gabriel@email.com\",\n" +
                                            "  \"senha\": \"senha123\",\n" +
                                            "  \"role\": \"USER\",\n" +
                                            "  \"nomeCompleto\": \"Gabriel da Silva\",\n" +
                                            "  \"dataNascimento\": \"2000-01-01T00:00:00.000+00:00\",\n" +
                                            "  \"endereco\": \"Rua ufms, 1\",\n" +
                                            "  \"cidade\": \"Campo Grande\",\n" +
                                            "  \"uf\": \"MS\",\n" +
                                            "  \"telefone\": \"67999998888\"\n" +
                                            "}"
                            )
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Usuário cadastrado com sucesso.", content = @Content(
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
                                            "  \"dataNascimento\": \"2000-01-01T00:00:00.000+00:00\",\n" +
                                            "  \"endereco\": \"Rua ufms, 1\",\n" +
                                            "  \"cidade\": \"Campo Grande\",\n" +
                                            "  \"uf\": \"MS\",\n" +
                                            "  \"telefone\": \"67999998888\"\n" +
                                            "}")
                            },
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Usuario.class)
                    )
                    ),
                    @ApiResponse(responseCode = "400", description = "Json inválido."),
            }
    )
    @ResponseBody
    ResponseEntity<?> register(@RequestBody RegistroDTO data);

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )

    @Operation(
            operationId = "logar",
            summary = "Efetuar login e gerar token de autenticação.",
            description = "Recebe os dados de email e senha do usuário e confere com os salvos no banco de dados.",
            tags = {"Usuário"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados de login",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Usuario.class),
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
                    @ApiResponse(responseCode = "201", description = "Usuário cadastrado com sucesso.", content = @Content(
                            examples = {@ExampleObject(
                                    name = "Exemplo login",
                                    summary = "",
                                    description = "Usuário logado com sucesso.",
                                    value = "{" +
                                            " \"token\": \"tokenmuitograndeaAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\""+
                                    "}")
                            },
                            mediaType = MediaType.APPLICATION_JSON_VALUE
                    )
                    ),
                    @ApiResponse(responseCode = "400", description = "Json inválido."),
            }
    )
    @ResponseBody
    ResponseEntity login(@RequestBody @Valid AutenticacaoDTO data);
}
