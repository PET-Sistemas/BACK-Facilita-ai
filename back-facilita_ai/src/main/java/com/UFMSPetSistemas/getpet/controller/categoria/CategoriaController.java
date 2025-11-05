package com.UFMSPetSistemas.getpet.controller.categoria;

import com.UFMSPetSistemas.getpet.controller.categoria.dto.AtualizarCategoriaDTO;
import com.UFMSPetSistemas.getpet.controller.categoria.dto.CadastroCategoriaDTO;
import com.UFMSPetSistemas.getpet.controller.categoria.dto.CategoriaResponseDTO;
import com.UFMSPetSistemas.getpet.model.entities.Categoria;
import com.UFMSPetSistemas.getpet.model.repository.CategoriaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class CategoriaController implements IntCategoriaController {
    private final CategoriaRepository categoriaRepository;

    public CategoriaController(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @Override
    public ResponseEntity<?> createCategoria(@RequestBody CadastroCategoriaDTO categoriaDTO) {
        System.out.println("Dados recebidos: " + categoriaDTO);

        try {
            // Verificar se a categoria já existe pelo título
            // if (categoriaRepository.existsByTitulo(categoriaDTO.titulo())) {
            // return ResponseEntity.badRequest().body(null); // Categoria não informada ou
            // inválida
            // }

            Categoria categoriaSalva = this.categoriaRepository.save(new Categoria(
                    categoriaDTO.titulo()));

            System.out.println("Categoria salva: " + categoriaSalva);

            return ResponseEntity.created(URI.create("/categories/" + categoriaSalva.getId())).body(categoriaSalva);
        } catch (Exception e) {
            System.err.println("Erro ao salvar: " + e.getMessage());
            e.printStackTrace();

            return ResponseEntity.unprocessableEntity().body(Map.of(
                    "errors", List.of(Map.of("message", e.getMessage()))));
        }
    }

    @Override
    public List<CategoriaResponseDTO> getAllCategorias() {
        return categoriaRepository.findAll().stream()
                .map(categoria -> new CategoriaResponseDTO(categoria.getTitulo()))
                .collect(Collectors.toList());
    }

    @Override
    public ResponseEntity<Categoria> getCategoriaById(Long id) {
        Optional<Categoria> categoria = categoriaRepository.findById(id);
        return categoria.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<Categoria> updateCategoria(@PathVariable Long id,
            @RequestBody AtualizarCategoriaDTO atualizarCategoriaDTO) {
        Optional<Categoria> categoriaExistente = categoriaRepository.findById(id);

        if (categoriaExistente.isPresent()) {
            Categoria categoria = categoriaExistente.get();

            // Atualizar apenas os atributos enviados
            if (atualizarCategoriaDTO.titulo() != null) {
                categoria.setTitulo(atualizarCategoriaDTO.titulo());
            }

            // Salvar alterações
            categoriaRepository.save(categoria);
            return ResponseEntity.ok(categoria);
        }

        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<Void> deleteCategoria(@PathVariable Long id) {
        if (categoriaRepository.existsById(id)) {
            categoriaRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
