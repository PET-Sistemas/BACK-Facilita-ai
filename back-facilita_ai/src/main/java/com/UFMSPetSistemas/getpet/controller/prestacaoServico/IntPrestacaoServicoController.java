package com.UFMSPetSistemas.getpet.controller.prestacaoServico;

import com.UFMSPetSistemas.getpet.controller.prestacaoServico.dto.AtualizarPrestacaoServicoDTO;
import com.UFMSPetSistemas.getpet.controller.prestacaoServico.dto.CadastrarPrestacaoServicoDTO;
import com.UFMSPetSistemas.getpet.controller.prestacaoServico.dto.PrestacaoServicoListDTO;
import com.UFMSPetSistemas.getpet.controller.prestacaoServico.dto.AvaliacaoResponseDTO;
import com.UFMSPetSistemas.getpet.model.entities.PrestacaoServico;
import com.UFMSPetSistemas.getpet.model.entities.Servico;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RequestMapping("/prestacoes-servico")
@Tag(name = "Prestação de Serviços", description = "Endpoints para o gerenciamento e consulta de prestações de serviço")
public interface IntPrestacaoServicoController {

        @PostMapping
        @Operation(summary = "Registrar uma nova prestação de serviço", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Dados do novo servico", required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation = Servico.class), examples = @ExampleObject(name = "Exemplo de serviço", summary = "JSON válido para criação de serviço", value = "{\n"
                        +
                        "  \"dataPrestacao\": \"2024-07-26\",\n" +
                        "  \"avaliacao\": \"2\",\n" +
                        "  \"avaliacaodesc\": \"Serviço rápido e eficiente.\",\n" +
                        "  \"servicoId\": \"1\",\n" +
                        "  \"usuarioConsumidor\": \"2\"\n" +
                        "}"))))
        @ApiResponse(responseCode = "201", description = "Prestação de serviço registrada com sucesso", content = @Content(schema = @Schema(implementation = PrestacaoServico.class)))
        ResponseEntity<PrestacaoServico> registrar(@RequestBody @Valid CadastrarPrestacaoServicoDTO dto);

        @GetMapping(path = "/todos")
        @Operation(summary = "Listar todas a prestações de serviços")
        public List<PrestacaoServico> getAll();

        @GetMapping(path = "/id")
        @Operation(summary = "Listar prestações de serviços por ID")
        public ResponseEntity<?> getPrestacaoServicoById(@RequestParam Long id);

        @GetMapping("/avaliacoes")
        @Operation(summary = "Listar serviços por faixa de avaliação")
        @Transactional(readOnly = true)
        @ApiResponse(responseCode = "200", description = "Lista de serviços encontrada", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Servico.class))))
        ResponseEntity<List<Servico>> listarPorAvaliacao(
                        @Parameter(in = ParameterIn.QUERY, name = "avaliacaoMinima") int avaliacaoMinima,
                        @Parameter(in = ParameterIn.QUERY, name = "avaliacaoMaxima") int avaliacaoMaxima);

        @GetMapping("/servico/{id}/avaliacoes")
        @Operation(summary = "Buscar avaliações pelo ID do serviço")
        @Transactional(readOnly = true)
        @ApiResponse(responseCode = "200", description = "Lista de avaliações encontrada", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = AvaliacaoResponseDTO.class))))
        @ApiResponse(responseCode = "404", description = "Serviço não encontrado", content = @Content)
        ResponseEntity<List<AvaliacaoResponseDTO>> findByServicoId(
                        @Parameter(in = ParameterIn.PATH, name = "id") @PathVariable Long id);

        @PatchMapping("/id/avaliacao")
        @Operation(summary = "Atualizar avaliação de um serviço prestado")
        @Transactional
        @ApiResponse(responseCode = "200", description = "Avaliação atualizada com sucesso", content = @Content(schema = @Schema(implementation = PrestacaoServico.class)))
        ResponseEntity<PrestacaoServico> atualizarAvaliacao(
                        @Parameter(in = ParameterIn.PATH, name = "id") Long id,
                        @RequestBody @Valid AtualizarPrestacaoServicoDTO dto);

        @DeleteMapping
        @Operation(summary = "Deletar uma prestação de serviço")
        public ResponseEntity<Void> deletePrestacaoServico(@RequestParam Long id);
}