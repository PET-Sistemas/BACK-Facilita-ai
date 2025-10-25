package com.UFMSPetSistemas.getpet.controller.usuario;

import java.util.List;

import com.UFMSPetSistemas.getpet.controller.usuario.dto.AtualizarUsuarioDTO;
import com.UFMSPetSistemas.getpet.controller.usuario.dto.ListarUsuariosDTO;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.UFMSPetSistemas.getpet.model.entities.Usuario;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RequestMapping("/usuario")
@Tag(name = "Usuário", description = "Endpoints para gerenciamento de usuários.")
public interface IntUsuarioController {

    @GetMapping(path = "/todos")
    @Operation(
            operationId = "listarUsuarios",
            summary = "Listar todos os usuários",
            description = "Retorna uma lista com todos os usuários cadastrados.",
            tags = {"Usuário"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuários retornados com sucesso.", content = @Content(
                            examples = {@ExampleObject(
                                    name = "Ex. 1: Lista cheia de usuários",
                                    summary = "Lista de usuários com + de 1 usuário retornados.",
                                    description = "",
                                    value = "[" +
                                            "  {" +
                                            "    \"id\": 1,\n" +
                                            "    \"nomeCompleto\": \"João da Silva\",\n" +
                                            "    \"dataNascimento\": \"1989-12-31\",\n" +
                                            "    \"endereco\": \"Rua das Flores, 123\",\n" +
                                            "    \"cidade\": \"Campo Grande\",\n" +
                                            "    \"uf\": \"MS\",\n" +
                                            "    \"email\": \"joao@example.com\",\n" +
                                            "    \"telefone\": \"67999998888\"\n" +
                                            "  },\n" +
                                            "  {\n" +
                                            "    \"id\": 2,\n" +
                                            "    \"nomeCompleto\": \"João da Silva\",\n" +
                                            "    \"dataNascimento\": \"1989-12-31\",\n" +
                                            "    \"endereco\": \"Rua das Flores, 123\",\n" +
                                            "    \"cidade\": \"Campo Grande\",\n" +
                                            "    \"uf\": \"MS\",\n" +
                                            "    \"email\": \"joao@example.com\",\n" +
                                            "    \"telefone\": \"67999998888\"\n" +
                                            "  }" +
                                            "]"
                            ),
                                    @ExampleObject(
                                            name = "Ex. 2: Lista vazia de usuários",
                                            summary = "Lista de usuários retornada vazia.",
                                            description = "",
                                            value = "[" +
                                                    "]"
                                    )
                            },
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Usuario.class)
                    )
                    ),
            }
    )
    public ResponseEntity<List<ListarUsuariosDTO>> listarUsuarios();

    @GetMapping(path = "/id")
    @Operation(
            operationId = "buscarPorId",
            summary = "Buscar usuário por ID",
            description = "Busca um usuário pelo seu ID.",
            tags = {"Usuário"},
            parameters = {
                    @Parameter(in = ParameterIn.PATH, name = "id", description = "ID do usuário a ser buscado", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuário retornado com sucesso.", content = @Content(schema = @Schema(implementation = Usuario.class))),
                    @ApiResponse(responseCode = "400", description = "ID inválido.")
            }
    )
    public ResponseEntity<?> buscarPorId(@RequestParam Long id);

    @GetMapping(path = "/nome")
    @Operation(
            operationId = "buscarPorNome",
            summary = "Buscar usuários por nome",
            description = "Busca usuários cujo nome contenha o valor informado.",
            tags = {"Usuário"},
            parameters = {
                    @Parameter(in = ParameterIn.PATH, name = "nome", description = "Nome ou parte do nome do usuário", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuário retornado com sucesso.", content = @Content(schema = @Schema(implementation = Usuario.class))),
                    @ApiResponse(responseCode = "400", description = "Nome inválido.")
            }
    )
    public ResponseEntity<?> buscarPorNome(@RequestParam String nome);

    @GetMapping(path = "/endereco")
    @Operation(
            operationId = "buscarPorEndereco",
            summary = "Buscar usuários por endereço",
            description = "Busca usuários cujo endereço contenha o valor informado.",
            tags = {"Usuário"},
            parameters = {
                    @Parameter(in = ParameterIn.PATH, name = "endereco", description = "Endereço ou parte do endereço do usuário", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuário encontrado com sucesso.", content = @Content(schema = @Schema(implementation = Usuario.class))),
                    @ApiResponse(responseCode = "400", description = "Endereço inválido.")
            }
    )
    public ResponseEntity<?> buscarPorEndereco(@RequestParam String endereco);

    @PutMapping
    @Operation(
            operationId = "putUsuario",
            summary = "Atualizar um usuário",
            description = "Atualiza os dados de um usuário existente.",
            tags = {"Usuário"},
            parameters = {
                    @Parameter(in = ParameterIn.PATH, name = "id", description = "ID do usuário a ser atualizado", required = true)
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados do usuário a ser atualizado",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Usuario.class),
                            examples = @ExampleObject(
                                    name = "Exemplo de usuário",
                                    summary = "JSON válido para atualização de usuário",
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
                    @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso.", content = @Content(
                            examples = {@ExampleObject(
                                    name = "Ex. 1: Dados do usuário atualizado",
                                    summary = "",
                                    description = "",
                                    value = "{" +
                                            "  \"id\": 1,\n" +
                                            "  \"nomeCompleto\": \"Felipe\",\n" +
                                            "  \"dataNascimento\": \"1989-12-31T00:00:00.000+00:00\",\n" +
                                            "  \"endereco\": \"Rua das Flores, 123\",\n" +
                                            "  \"cidade\": \"Campo Grande\",\n" +
                                            "  \"uf\": \"MS\",\n" +
                                            "  \"email\": \"joao@example.com\",\n" +
                                            "  \"telefone\": \"67999998888\",\n" +
                                            "  \"senha\": \"senha123\"\n" +
                                            "}"
                            ),
                                    @ExampleObject(
                                            name = "Ex. 2: ID inválido",
                                            summary = "",
                                            description = "",
                                            value = "Usuario com ID 0 não encontrado."
                                    )
                            },
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Usuario.class)
                    )
                    ),
                    @ApiResponse(responseCode = "422", description = "Erro ao atualizar usuário.")
            }
    )
    public ResponseEntity<?> putUsuario(@RequestBody @Valid AtualizarUsuarioDTO data, @RequestParam Long id);

    @DeleteMapping
    @Operation(
            operationId = "deleteColaborador",
            summary = "Deletar um usuário",
            description = "Remove um usuário do banco de dados pelo ID.",
            tags = {"Usuário"},
            parameters = {
                    @Parameter(in = ParameterIn.PATH, name = "id", description = "ID do usuário a ser deletado", required = true)
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Usuário deletado com sucesso.", content = @Content(
                            examples = @ExampleObject(
                                    name = "Usuário deletado com sucesso.",
                                    summary = "",
                                    description = "",
                                    value = "Sem retorno."
                            ),
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Usuario.class)
                    )
                    ),
            }
    )
    public void deleteColaborador(@RequestParam Long id);
}
