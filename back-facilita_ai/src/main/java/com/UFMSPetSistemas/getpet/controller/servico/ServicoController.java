package com.UFMSPetSistemas.getpet.controller.servico;

import com.UFMSPetSistemas.getpet.controller.servico.dto.ServicoResponseDTO;
import com.UFMSPetSistemas.getpet.controller.servico.dto.AtualizarServicoDTO;
import com.UFMSPetSistemas.getpet.controller.servico.dto.CadastroServicoDTO;
import com.UFMSPetSistemas.getpet.controller.servico.dto.ServicoDetalhadoDTO;
import com.UFMSPetSistemas.getpet.controller.servico.dto.PrestadorDTO;
import com.UFMSPetSistemas.getpet.model.entities.Servico;
import com.UFMSPetSistemas.getpet.model.entities.Categoria;
import com.UFMSPetSistemas.getpet.model.entities.Usuario;
import com.UFMSPetSistemas.getpet.model.repository.ServicoRepository;
import com.UFMSPetSistemas.getpet.model.repository.CategoriaRepository;
import com.UFMSPetSistemas.getpet.model.repository.UsuarioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class ServicoController implements IntServicoController {
    private final ServicoRepository servicoRepository;
    private final CategoriaRepository categoriaRepository;
    private final UsuarioRepository usuarioRepository;

    public ServicoController(ServicoRepository servicoRepository, CategoriaRepository categoriaRepository,
            UsuarioRepository usuarioRepository) {
        this.servicoRepository = servicoRepository;
        this.categoriaRepository = categoriaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public ResponseEntity<?> createServico(@RequestBody CadastroServicoDTO servicoDTO) {
        System.out.println("Dados recebidos: " + servicoDTO);

        try {
            if (servicoDTO.categoriaNome() == null || servicoDTO.categoriaNome().isBlank()) {
                return ResponseEntity.badRequest().body("Nome da categoria não informado ou inválido.");
            }

            Optional<Categoria> categoria = categoriaRepository.findByTitulo(servicoDTO.categoriaNome());

            if (categoria.isEmpty()) {
                return ResponseEntity.badRequest().body("Categoria não encontrada!");
            }

            Optional<Usuario> usuarioPrestador = usuarioRepository.findById(servicoDTO.usuarioPrestadorID());

            if (usuarioPrestador.isEmpty()) {
                return ResponseEntity.badRequest().body("Usuario prestador não encontrado!");
            }

            Servico servicoSalvo = this.servicoRepository.save(new Servico(
                    servicoDTO.titulo(),
                    servicoDTO.descricao(),
                    servicoDTO.valor(),
                    categoria.get(),
                    usuarioPrestador.get()));

            System.out.println("Servico salvo: " + servicoSalvo);

            return ResponseEntity.created(URI.create("/servicos/" + servicoSalvo.getId())).body(servicoSalvo);
        } catch (Exception e) {
            System.err.println("Erro ao salvar: " + e.getMessage());
            e.printStackTrace();

            return ResponseEntity.unprocessableEntity().body(Map.of(
                    "errors", List.of(Map.of("message", e.getMessage()))));
        }
    }

    @Override
    public List<ServicoResponseDTO> getAllServicos() {
        return servicoRepository.findAll().stream()
                .map(servico -> new ServicoResponseDTO(
                        servico.getId(),
                        servico.getTitulo(),
                        servico.getDescricao(),
                        servico.getValor(),
                        servico.getCategoria().getTitulo())) // Adicionado o nome da categoria
                .collect(Collectors.toList());
    }

    @Override
    public ResponseEntity<ServicoDetalhadoDTO> getServicoById(Long id) {
        Optional<Servico> servicoOptional = servicoRepository.findById(id);

        if (servicoOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Servico servico = servicoOptional.get();
        Usuario prestador = servico.getUsuarioPrestador();

        String enderecoCompleto = String.format("%s, %s - %s",
                prestador.getEndereco(),
                prestador.getCidade(),
                prestador.getUf());

        PrestadorDTO prestadorDTO = new PrestadorDTO(
                prestador.getNomeCompleto(),
                enderecoCompleto,
                prestador.getTelefone(),
                0, // TODO: Implementar lógica de média de avaliações
                0 // TODO: Implementar lógica de total de avaliações
        );

        ServicoDetalhadoDTO servicoDetalhadoDTO = new ServicoDetalhadoDTO(
                servico.getId(),
                servico.getTitulo(),
                servico.getDescricao(),
                servico.getValor(),
                prestadorDTO);

        return ResponseEntity.ok(servicoDetalhadoDTO);
    }

    @Override
    public List<Servico> getServicosByUsuarioPrestadorEndereco(@RequestParam String endereco) {
        return servicoRepository.findByUsuarioPrestadorEndereco(endereco);
    }

    @Override
    public List<Servico> getServicosByCategoria(@PathVariable Long id) {
        Optional<Categoria> categoria = categoriaRepository.findById(id);
        return categoria.map(servicoRepository::findByCategoria).orElse(List.of());
    }

    @Override
    public List<Servico> getServicosByValor(@RequestParam double minValor, @RequestParam double maxValor) {
        return servicoRepository.findByValorBetween(minValor, maxValor);
    }

    @Override
    public ResponseEntity<Servico> updateServico(@PathVariable Long id,
            @RequestBody AtualizarServicoDTO servicoAtualizadoDTO) {
        Optional<Servico> servicoExistente = servicoRepository.findById(id);
        Optional<Categoria> categoria = categoriaRepository.findById(servicoAtualizadoDTO.categoriaID());
        Optional<Usuario> usuarioPrestador = usuarioRepository.findById(servicoAtualizadoDTO.usuarioPrestadorID());

        if (servicoExistente.isPresent()) {
            Servico servico = servicoExistente.get();

            // Atualizar apenas os atributos enviados
            if (servicoAtualizadoDTO.titulo() != null) {
                servico.setTitulo(servicoAtualizadoDTO.titulo());
            }
            if (servicoAtualizadoDTO.descricao() != null) {
                servico.setDescricao(servicoAtualizadoDTO.descricao());
            }
            if (servicoAtualizadoDTO.valor() != 0) {
                servico.setValor(servicoAtualizadoDTO.valor());
            }

            categoria.ifPresent(servico::setCategoria);

            usuarioPrestador.ifPresent(servico::setUsuarioPrestador);

            // Salvar alterações
            servicoRepository.save(servico);
            return ResponseEntity.ok(servico);
        }

        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<Void> deleteServico(@PathVariable Long id) {
        if (servicoRepository.existsById(id)) {
            servicoRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
