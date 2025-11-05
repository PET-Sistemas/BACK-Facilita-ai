package com.UFMSPetSistemas.getpet.controller.categoria;

import com.UFMSPetSistemas.getpet.controller.categoria.dto.AtualizarCategoriaDTO;
import com.UFMSPetSistemas.getpet.controller.categoria.dto.CadastroCategoriaDTO;
import com.UFMSPetSistemas.getpet.controller.categoria.dto.CategoriaResponseDTO;
import com.UFMSPetSistemas.getpet.model.entities.Categoria;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RequestMapping("/categoria")
@Tag(name = "Categoria", description = "Endpoints para gerenciamento de categorias.")
public interface IntCategoriaController {
        // Criar nova categoria
        @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
        @Transactional
        @Operation(operationId = "cadastrarCategoria", summary = "Cadastrar uma nova categoria.", description = "Recebe os dados de uma nova categoria e salva no banco de dados.", tags = {
                        "Categoria" }, requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Dados da nova categoria", required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = Categoria.class), examples = @ExampleObject(name = "Exemplo de categoria", summary = "JSON válido para criação de categoria", value = "{\n"
                                        +
                                        "  \"titulo\": \"Categoria1\"\n" +
                                        "}"))), responses = {
                                                        @ApiResponse(responseCode = "201", description = "Categoria cadastrada com sucesso.", content = @Content(examples = {
                                                                        @ExampleObject(name = "Nova Categoria Exemplo", summary = "", description = "Categoria cadastrada com todos os campos", value = "{"
                                                                                        +
                                                                                        "  \"id\": 1,\n" +
                                                                                        "  \"titulo\": \"Categoria1\"\n"
                                                                                        +
                                                                                        "}")
                                                        }, mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Categoria.class))),
                                                        @ApiResponse(responseCode = "400", description = "Json inválido."),
                                        })
        @ResponseBody
        public ResponseEntity<?> createCategoria(@RequestBody CadastroCategoriaDTO categoriaDTO);

        // Buscar todas as categorias
        @GetMapping(path = "/todas")
        @Operation(operationId = "listarCategorias", summary = "Listar todas as categorias", description = "Retorna uma lista com todas as categorias cadastradas.", tags = {
                        "Categoria" }, responses = {
                                        @ApiResponse(responseCode = "200", description = "Categorias retornadas com sucesso.", content = @Content(examples = {
                                                        @ExampleObject(name = "Ex. 1: Lista cheia de categorias", summary = "Lista de categorias com + de 1 categorias retornadas.", description = "", value = "[\n"
                                                                        +
                                                                        "  {\n" +
                                                                        "    \"titulo\": \"Limpeza\"\n" +
                                                                        "  },\n" +
                                                                        "  {\n" +
                                                                        "    \"titulo\": \"Manutenção\"\n" +
                                                                        "  }\n" +
                                                                        "]"),
                                                        @ExampleObject(name = "Ex. 2: Lista vazia de categorias", summary = "Lista de categorias retornada vazia.", description = "", value = "[]")
                                        }, mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = CategoriaResponseDTO.class))),
                        })
        @ResponseBody
        public List<CategoriaResponseDTO> getAllCategorias();

        // Buscar categoria por ID
        @GetMapping("/id")
        @Operation(operationId = "buscarPorId", summary = "Buscar categoria por ID", description = "Busca uma categoria pelo seu ID.", tags = {
                        "Categoria" }, parameters = {
                                        @Parameter(in = ParameterIn.PATH, name = "id", description = "ID da categoria a ser buscada", required = true)
                        }, responses = {
                                        @ApiResponse(responseCode = "200", description = "Categoria retornada com sucesso.", content = @Content(schema = @Schema(implementation = Categoria.class))),
                                        @ApiResponse(responseCode = "400", description = "ID inválido.")
                        })
        public ResponseEntity<?> getCategoriaById(@RequestParam Long id);

        // Atualizar categoria (suporte a atualização parcial)
        @PutMapping
        @Transactional
        @Operation(operationId = "putCategoria", summary = "Atualizar uma categoria", description = "Atualiza os dados de uma categoria existente.", tags = {
                        "Categoria" }, parameters = {
                                        @Parameter(in = ParameterIn.PATH, name = "id", description = "ID da categoria a ser atualizada", required = true)
                        }, requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Dados da categoria a ser atualizada", required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = Categoria.class), examples = @ExampleObject(name = "Exemplo de categoria", summary = "JSON válido para atualização de categoria", value = "{"
                                        +
                                        "  \"titulo\": \"Categoria1 Atualizada\"\n" +
                                        "}"))), responses = {
                                                        @ApiResponse(responseCode = "200", description = "Categoria atualizada com sucesso.", content = @Content(examples = {
                                                                        @ExampleObject(name = "Ex. 1: Dados da categoria atualizada", summary = "", description = "", value = "{"
                                                                                        +
                                                                                        "  \"id\": 1,\n" +
                                                                                        "  \"titulo\": \"Categoria1 Atualizada\"\n"
                                                                                        +
                                                                                        "}"),
                                                                        @ExampleObject(name = "Ex. 2: ID inválido", summary = "", description = "", value = "Categoria com ID 0 não encontrado.")
                                                        }, mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Categoria.class))),
                                                        @ApiResponse(responseCode = "422", description = "Erro ao atualizar categoria.")
                                        })
        public ResponseEntity<?> updateCategoria(@RequestParam Long id,
                        @RequestBody AtualizarCategoriaDTO categoriaAtualizadaDTO);

        // Deletar categoria
        @DeleteMapping
        @Operation(operationId = "deleteCategoria", summary = "Deletar uma categoria", description = "Remove uma categoria do banco de dados pelo ID.", tags = {
                        "Categoria" }, parameters = {
                                        @Parameter(in = ParameterIn.PATH, name = "id", description = "ID da categoria a ser deletada", required = true)
                        }, responses = {
                                        @ApiResponse(responseCode = "200", description = "Categoria deletada com sucesso.", content = @Content(examples = @ExampleObject(name = "Categoria deletada com sucesso.", summary = "", description = "", value = "Sem retorno."), mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Categoria.class))),
                        })
        public ResponseEntity<Void> deleteCategoria(@RequestParam Long id);
}