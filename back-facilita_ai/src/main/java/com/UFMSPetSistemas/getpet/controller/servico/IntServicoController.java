package com.UFMSPetSistemas.getpet.controller.servico;

import com.UFMSPetSistemas.getpet.controller.servico.dto.AtualizarServicoDTO;
import com.UFMSPetSistemas.getpet.controller.servico.dto.CadastroServicoDTO;
import com.UFMSPetSistemas.getpet.controller.servico.dto.ServicoAvaliacaoDTO;
import com.UFMSPetSistemas.getpet.controller.servico.dto.ServicoDetalhadoDTO;
import com.UFMSPetSistemas.getpet.controller.servico.dto.ServicoResponseDTO;
import com.UFMSPetSistemas.getpet.model.entities.Servico;
import com.UFMSPetSistemas.getpet.model.entities.Usuario;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RequestMapping("/servico")
@Tag(name = "Serviço", description = "Endpoints para gerenciamento de serviços.")
public interface IntServicoController {
        // Criar novo serviço
        @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
        @Transactional
        @Operation(operationId = "cadastrarServico", summary = "Cadastrar um novo servico.", description = "Recebe os dados de um novo servico e o salva no banco de dados.", tags = {
                        "Serviço" }, requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Dados do novo servico", required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = Servico.class), examples = @ExampleObject(name = "Exemplo de serviço", summary = "JSON válido para criação de serviço", value = "{\n"
                                        +
                                        "  \"titulo\": \"Servico de Pintura\",\n" +
                                        "  \"descricao\": \"Pintura de paredes com tinta acrílica.\",\n" +
                                        "  \"valor\": 150.00,\n" +
                                        "  \"categoriaNome\": \"Pintura\",\n" +
                                        "  \"usuarioPrestadorId\": 1\n" +
                                        "}"))), responses = {
                                                        @ApiResponse(responseCode = "201", description = "Servico cadastrado com sucesso.", content = @Content(examples = {
                                                                        @ExampleObject(name = "Novo Serviço Exemplo", summary = "", description = "Serviço cadastrado com todos os campos", value = "{"
                                                                                        +
                                                                                        "  \"id\": 1,\n" +
                                                                                        "  \"titulo\": \"Servico de Pintura\",\n"
                                                                                        +
                                                                                        "  \"descricao\": \"Pintura de paredes com tinta acrílica.\",\n"
                                                                                        +
                                                                                        "  \"valor\": 150.00,\n" +
                                                                                        "  \"categoria\": { \"id\": 1, \"titulo\": \"Pintura\" },\n"
                                                                                        +
                                                                                        "  \"usuarioPrestadorId\": 1\n"
                                                                                        +
                                                                                        "}")
                                                        }, mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Usuario.class))),
                                                        @ApiResponse(responseCode = "400", description = "Json inválido."),
                                        })
        @ResponseBody
        public ResponseEntity<?> createServico(@RequestBody CadastroServicoDTO servicoDTO, @AuthenticationPrincipal Usuario usuarioLogado);

        // Buscar todos os serviços
        @GetMapping(path = "/todos")
        @Operation(operationId = "listarServiços", summary = "Listar todos os serviços", description = "Retorna uma lista com todos os serviços cadastrados.", tags = {
                        "Serviço" }, responses = {
                                        @ApiResponse(responseCode = "200", description = "Serviços retornados com sucesso.", content = @Content(examples = {
                                                        @ExampleObject(name = "Ex. 1: Lista cheia de serviços", summary = "Lista de serviços com + de 1 serviço retornados.", description = "", value = "["
                                                                        +
                                                                        "  {" +
                                                                        "    \"titulo\": \"Servico de Pintura\",\n" +
                                                                        "    \"descricao\": \"Pintura de paredes com tinta acrílica.\",\n"
                                                                        +
                                                                        "    \"valor\": 150.00,\n" +
                                                                        "    \"categoriaNome\": \"Pintura\"\n" +
                                                                        "  },\n" +
                                                                        "  {\n" +
                                                                        "    \"titulo\": \"Servico de Limpeza\",\n" +
                                                                        "    \"descricao\": \"Limpeza completa de apartamento.\",\n"
                                                                        +
                                                                        "    \"valor\": 200.00,\n" +
                                                                        "    \"categoriaNome\": \"Limpeza\"\n" +
                                                                        "  }" +
                                                                        "]"),
                                                        @ExampleObject(name = "Ex. 2: Lista vazia de serviços", summary = "Lista de serviços retornada vazia.", description = "", value = "["
                                                                        +
                                                                        "]")
                                        }, mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Usuario.class))),
                        })
        public List<ServicoResponseDTO> getAllServicos();

        // Buscar serviço por ID
        @GetMapping("/{id}")
        @Operation(operationId = "buscarPorId", summary = "Buscar servico detalhado por ID", description = "Busca um servico pelo seu ID e retorna seus detalhes, incluindo informações do prestador.", tags = {
                        "Serviço" }, parameters = {
                                        @Parameter(in = ParameterIn.PATH, name = "id", description = "ID do servico a ser buscado", required = true, schema = @Schema(type = "integer", format = "int64"))
                        }, responses = {
                                        @ApiResponse(responseCode = "200", description = "Servico retornado com sucesso.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ServicoDetalhadoDTO.class), examples = @ExampleObject(value = "{\n"
                                                        +
                                                        "  \"id\": 1,\n" +
                                                        "  \"titulo\": \"Montador de Móveis\",\n" +
                                                        "  \"descricao\": \"Montagem profissional de todos os tipos de móveis.\",\n"
                                                        +
                                                        "  \"valor\": 550.00,\n" +
                                                        "  \"prestador\": {\n" +
                                                        "    \"nome\": \"Alexandre Soares\",\n" +
                                                        "    \"enderecoCompleto\": \"Rua 11 de setembro, nº 70, Campo Grande - MS\",\n"
                                                        +
                                                        "    \"telefoneWhatsapp\": \"+5567999440350\",\n" +
                                                        "    \"mediaAvaliacoes\": 3.5,\n" +
                                                        "    \"totalAvaliacoes\": 15\n" +
                                                        "  }\n" +
                                                        "}"))),
                                        @ApiResponse(responseCode = "404", description = "Serviço não encontrado.")
                        })
        public ResponseEntity<ServicoDetalhadoDTO> getServicoById(@PathVariable Long id);

        // Buscar serviços por endereço do usuário
        @GetMapping("/usuario-endereco")
        @Transactional(readOnly = true)
        @Operation(operationId = "buscarServicosPorEndereco", summary = "Buscar servicos por endereço", description = "Busca servicos por endereço do usuário prestador.", tags = {
                        "Serviço" }, parameters = {
                                        @Parameter(in = ParameterIn.PATH, name = "endereco", description = "Endereço ou parte do endereço do usuário", required = true)
                        }, responses = {
                                        @ApiResponse(responseCode = "200", description = "Servico encontrado com sucesso.", content = @Content(schema = @Schema(implementation = Usuario.class))),
                                        @ApiResponse(responseCode = "400", description = "Endereço inválido.")
                        })
        public List<Servico> getServicosByUsuarioPrestadorEndereco(@RequestParam String endereco);

        // Buscar serviços por categoria
        @GetMapping("/categoria/id")
        @Transactional(readOnly = true)
        @Operation(operationId = "buscarPorIdDaCategoria", summary = "Buscar servicos por ID da Categoria", description = "Busca servicos pelo ID da Categoria.", tags = {
                        "Serviço" }, parameters = {
                                        @Parameter(in = ParameterIn.PATH, name = "id", description = "ID da categoria a ser buscada", required = true)
                        }, responses = {
                                        @ApiResponse(responseCode = "200", description = "Servicos retornados com sucesso.", content = @Content(schema = @Schema(implementation = Usuario.class))),
                                        @ApiResponse(responseCode = "400", description = "ID de categoria inválido.")
                        })
        public List<Servico> getServicosByCategoria(@PathVariable Long id);

        // Buscar serviços por valor entre um intervalo
        @GetMapping("/valor")
        @Operation(operationId = "buscarPorValor", summary = "Buscar servicos por valor", description = "Busca servicos pelo ID da Categoria.", tags = {
                        "Serviço" }, parameters = {
                                        @Parameter(in = ParameterIn.PATH, name = "minValor", description = "Valor mínimo dos servicos a serem buscados", required = true),
                                        @Parameter(in = ParameterIn.PATH, name = "maxValor", description = "Valor máximo dos servicos a serem buscados", required = true)
                        }, responses = {
                                        @ApiResponse(responseCode = "200", description = "Servicos retornados com sucesso.", content = @Content(schema = @Schema(implementation = Usuario.class))),
                                        @ApiResponse(responseCode = "400", description = "ID de categoria inválido.")
                        })
        public List<Servico> getServicosByValor(@RequestParam double minValor, @RequestParam double maxValor);

        // Atualizar serviço (suporte a atualização parcial)
        @PutMapping
        @Transactional
        @Operation(operationId = "putServico", summary = "Atualizar um serviço", description = "Atualiza os dados de um serviço existente.", tags = {
                        "Serviço" }, parameters = {
                                        @Parameter(in = ParameterIn.PATH, name = "id", description = "ID do serviço a ser atualizado", required = true)
                        }, requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Dados do serviço a ser atualizado", required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = Usuario.class), examples = @ExampleObject(name = "Exemplo de serviço", summary = "JSON válido para atualização de serviço", value = "{"
                                        +
                                        "  \"titulo\": \"Servico1 Atualizado\",\n" +
                                        "  \"descricao\": \"Servico legal atualizado.\",\n" +
                                        "  \"valor\": 123.45,\n" +
                                        "  \"categoriaNome\": \"Manutenção\",\n" +
                                        "  \"usuarioPrestadorId\": 1\n" +
                                        "}"))), responses = {
                                                        @ApiResponse(responseCode = "200", description = "Servico atualizado com sucesso.", content = @Content(examples = {
                                                                        @ExampleObject(name = "Ex. 1: Dados do servico atualizado", summary = "", description = "", value = "{"
                                                                                        +
                                                                                        "  \"id\": 1,\n" +
                                                                                        "  \"titulo\": \"Servico1 Atualizado\",\n"
                                                                                        +
                                                                                        "  \"descricao\": \"Servico legal atualizado.\",\n"
                                                                                        +
                                                                                        "  \"valor\": 123.45,\n" +
                                                                                        "  \"categoria\": { \"id\": 2, \"titulo\": \"Manutenção\" },\n"
                                                                                        +
                                                                                        "  \"usuarioPrestadorId\": 1\n"
                                                                                        +
                                                                                        "}"),
                                                                        @ExampleObject(name = "Ex. 2: ID inválido", summary = "", description = "", value = "Servico com ID 0 não encontrado.")
                                                        }, mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Servico.class))),
                                                        @ApiResponse(responseCode = "422", description = "Erro ao atualizar servico.")
                                        })
        public ResponseEntity<?> updateServico(@RequestParam Long id,
                        @RequestBody AtualizarServicoDTO servicoAtualizadoDTO);

        // Deletar serviço
        @DeleteMapping
        @Operation(operationId = "deleteServiço", summary = "Deletar um serviço", description = "Remove um serviço do banco de dados pelo ID.", tags = {
                        "Serviço" }, parameters = {
                                        @Parameter(in = ParameterIn.PATH, name = "id", description = "ID do serviço a ser deletado", required = true)
                        }, responses = {
                                        @ApiResponse(responseCode = "200", description = "Serviço deletado com sucesso.", content = @Content(examples = @ExampleObject(name = "Serviço deletado com sucesso.", summary = "", description = "", value = "Sem retorno."), mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Usuario.class))),
                        })
        public ResponseEntity<Void> deleteServico(@RequestParam Long id);

        // Buscar dados do serviço para avaliação
        @GetMapping("/{id}/avaliacao")
        @Operation(summary = "Buscar dados do serviço para avaliação")
        ResponseEntity<ServicoAvaliacaoDTO> getServicoParaAvaliacao(@PathVariable Long id);
}