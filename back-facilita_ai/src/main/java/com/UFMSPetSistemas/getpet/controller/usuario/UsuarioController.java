package com.UFMSPetSistemas.getpet.controller.usuario;

import com.UFMSPetSistemas.getpet.controller.usuario.dto.AtualizarUsuarioDTO;
import com.UFMSPetSistemas.getpet.controller.usuario.dto.CadastroUsuarioDTO;
import com.UFMSPetSistemas.getpet.controller.usuario.dto.ListarUsuariosDTO;
import com.UFMSPetSistemas.getpet.model.entities.Usuario;
import com.UFMSPetSistemas.getpet.model.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
@CrossOrigin
public class UsuarioController implements IntUsuarioController {
    @Autowired
    private UsuarioRepository repo;

    @Override
    public ResponseEntity<?> cadastrarUsuario(final CadastroUsuarioDTO novoColaborador) {
        System.out.println("Dados recebidos: " + novoColaborador);

        try {
            Usuario usuarioSalvo = this.repo.save(new Usuario(
                    novoColaborador.nomeCompleto(),
                    novoColaborador.dataNascimento(),
                    novoColaborador.endereco(),
                    novoColaborador.cidade(),
                    novoColaborador.uf(),
                    novoColaborador.email(),
                    novoColaborador.telefone(),
                    novoColaborador.senha()
            ));
            System.out.println("Usuário salvo: " + usuarioSalvo);

            return ResponseEntity.created(URI.create("/categories/" + usuarioSalvo.getId())).body(usuarioSalvo);
        } catch (Exception e) {
            System.err.println("Erro ao salvar: " + e.getMessage());
            e.printStackTrace();

            return ResponseEntity.unprocessableEntity().body(Map.of(
                    "errors", List.of(Map.of("message", e.getMessage()))
            ));
        }
    }

    @Override
    public ResponseEntity<List<ListarUsuariosDTO>> listarUsuarios() {

        List<Usuario> usuarios = repo.findAll();
        List<ListarUsuariosDTO> listaUsuarios = usuarios.stream()
                .map(ListarUsuariosDTO::new)
                .toList();

        return ResponseEntity.ok(listaUsuarios);
    }

    @Override
    public ResponseEntity<?> buscarPorId(final Long id) {
        try {
            final Usuario usuario = this.repo
                    .findById(id)
                    .orElseThrow(() -> new RuntimeException(String.format("Usuario com id %d não encontrado", id)));
            ListarUsuariosDTO dto = new ListarUsuariosDTO(usuario);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            System.out.println("Erro ao buscar por id: " + e.getMessage());
            e.printStackTrace();

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ID Inválido: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> buscarPorNome(final String nome) {
        try {
            final List<Usuario> usuarios = this.repo.findByNomeCompletoContaining(nome);
            if (usuarios.isEmpty()) {return  ResponseEntity.notFound().build();}
            List<ListarUsuariosDTO> listaUsuarios = usuarios.stream()
                    .map(ListarUsuariosDTO::new)
                    .toList();
            return ResponseEntity.ok(listaUsuarios);
        } catch (Exception e) {
            System.out.println("Erro ao buscar por nome: " + e.getMessage());
            e.printStackTrace();

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Nome Inválido: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> buscarPorEndereco(final String endereco) {
        try {
            final List<Usuario> usuarios = this.repo.findByEnderecoContaining(endereco);
            if (usuarios.isEmpty()) {return  ResponseEntity.notFound().build();}
            List<ListarUsuariosDTO> listaUsuarios = usuarios.stream()
                    .map(ListarUsuariosDTO::new)
                    .toList();
            return ResponseEntity.ok(listaUsuarios);
        } catch (Exception e) {
            System.out.println("Erro ao buscar por endereço: " + e.getMessage());
            e.printStackTrace();

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Endereco Inválido: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> putUsuario(final AtualizarUsuarioDTO newUsuario, final Long id) {
        try {
            //Usuario oldColaborador = this.repo.findById(id).isPresent() ? this.repo.findById(id).get() : new Usuario();
            Usuario usuario = this.repo.findById(id).orElseThrow(() -> new Exception("Usuario com ID " + id + " não encontrado."));

            usuario.update(
                    newUsuario.nomeCompleto(),
                    newUsuario.dataNascimento(),
                    newUsuario.endereco(),
                    newUsuario.cidade(),
                    newUsuario.uf(),
                    newUsuario.email(),
                    newUsuario.telefone(),
                    newUsuario.senha()
            );

            Usuario usuarioSalvo = this.repo.save(usuario);

            return ResponseEntity.ok().body(usuarioSalvo);
        } catch (Exception e) {
            System.err.println("Erro ao atualizar: " + e.getMessage());
            e.printStackTrace();

            return ResponseEntity.unprocessableEntity().body(e.getMessage());
        }
    }

    @Override
    public void deleteColaborador(final Long id) {
        this.repo.deleteById(id);
    }
}
