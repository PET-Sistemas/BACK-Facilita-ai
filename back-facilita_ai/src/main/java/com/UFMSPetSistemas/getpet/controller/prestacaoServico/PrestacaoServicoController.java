package com.UFMSPetSistemas.getpet.controller.prestacaoServico;

import com.UFMSPetSistemas.getpet.controller.prestacaoServico.dto.AtualizarPrestacaoServicoDTO;
import com.UFMSPetSistemas.getpet.controller.prestacaoServico.dto.AvaliacaoResponseDTO;
import com.UFMSPetSistemas.getpet.controller.prestacaoServico.dto.CadastrarPrestacaoServicoDTO;
import com.UFMSPetSistemas.getpet.model.entities.PrestacaoServico;
import com.UFMSPetSistemas.getpet.model.entities.Servico;
import com.UFMSPetSistemas.getpet.model.entities.Usuario;
import com.UFMSPetSistemas.getpet.model.repository.PrestacaoServicoRepository;
import com.UFMSPetSistemas.getpet.model.repository.ServicoRepository;
import com.UFMSPetSistemas.getpet.model.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.time.LocalDate;

@RestController
@CrossOrigin
public class PrestacaoServicoController implements IntPrestacaoServicoController {
    private static final Logger logger = LoggerFactory.getLogger(PrestacaoServicoController.class);
    @Autowired
    private PrestacaoServicoRepository prestacaoServicoRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private ServicoRepository servicoRepository;

    @Override
    @Transactional
    public ResponseEntity<PrestacaoServico> registrar(CadastrarPrestacaoServicoDTO dto) {
        Usuario cliente = usuarioRepository.findById(dto.usuarioConsumidor())
                .orElseThrow(
                        () -> new EntityNotFoundException("Cliente não encontrado com ID: " + dto.usuarioConsumidor()));
        Usuario prestador = usuarioRepository.findById(dto.usuarioPrestador())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Prestador não encontrado com ID: " + dto.usuarioPrestador()));
        Servico servico = servicoRepository.findById(dto.servicoId())
                .orElseThrow(() -> new EntityNotFoundException("Serviço não encontrado com ID: " + dto.servicoId()));

        PrestacaoServico novaPrestacao = new PrestacaoServico();
        novaPrestacao.setUsuarioConsumidor(cliente);
        novaPrestacao.setUsuarioPrestador(prestador);
        novaPrestacao.setServico(servico);
        novaPrestacao.setDataprestacao(Date.from(dto.dataPrestacao().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        if (dto.avaliacao() != null) {
            novaPrestacao.setAvaliacao(dto.avaliacao());
        }

        if (dto.avaliacaodesc() != null) {
            novaPrestacao.setAvaliacaodesc(dto.avaliacaodesc());
        }

        PrestacaoServico prestacaoSalva = prestacaoServicoRepository.save(novaPrestacao);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(prestacaoSalva.getId()).toUri();

        return ResponseEntity.created(uri).body(prestacaoSalva);
    }

    @Override
    public List<PrestacaoServico> getAll() {
        return prestacaoServicoRepository.findAll();
    }

    @Override
    public ResponseEntity<PrestacaoServico> getPrestacaoServicoById(Long id) {
        Optional<PrestacaoServico> prestacaoServico = prestacaoServicoRepository.findById(id);
        return prestacaoServico.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<List<Servico>> listarPorAvaliacao(int avaliacaoMinima, int avaliacaoMaxima) {
        List<Servico> servicos = prestacaoServicoRepository.findServicosByAvaliacaoBetween(avaliacaoMinima,
                avaliacaoMaxima);
        return ResponseEntity.ok(servicos);
    }

    @Override
    public ResponseEntity<List<AvaliacaoResponseDTO>> findByServicoId(@PathVariable Long id) {
        logger.info("Buscando avaliações para o serviço com ID: {}", id);
        List<PrestacaoServico> prestacoes = prestacaoServicoRepository.findByServicoId(id);
        logger.info("Encontradas {} prestações de serviço para o ID: {}", prestacoes.size(), id);

        List<AvaliacaoResponseDTO> resultadoDTO = prestacoes.stream()
                .map(this::converterParaAvaliacaoDTO)
                .collect(Collectors.toCollection(ArrayList::new));

        logger.info("Retornando {} DTOs de avaliação para o serviço ID: {}", resultadoDTO.size(), id);
        return ResponseEntity.ok(resultadoDTO);
    }

    @Override
    public ResponseEntity<PrestacaoServico> atualizarAvaliacao(Long id, AtualizarPrestacaoServicoDTO dto) {
        PrestacaoServico prestacao = prestacaoServicoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Prestação de Serviço não encontrada com ID: " + id));

        if (dto.avaliacao() != null) {
            prestacao.setAvaliacao(dto.avaliacao());
        }

        PrestacaoServico prestacaoAtualizada = prestacaoServicoRepository.save(prestacao);

        return ResponseEntity.ok(prestacaoAtualizada);
    }

    @Override
    public ResponseEntity<Void> deletePrestacaoServico(Long id) {
        if (prestacaoServicoRepository.existsById(id)) {
            prestacaoServicoRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    private AvaliacaoResponseDTO converterParaAvaliacaoDTO(PrestacaoServico p) {
        LocalDate dataPrestacao = new java.sql.Date(p.getDataprestacao().getTime()).toLocalDate();
        return new AvaliacaoResponseDTO(
                dataPrestacao,
                p.getAvaliacao(),
                p.getAvaliacaodesc(),
                p.getUsuarioConsumidor().getNomeCompleto());
    }
}