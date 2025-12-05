package com.UFMSPetSistemas.getpet.controller.usuario;

import com.UFMSPetSistemas.getpet.controller.usuario.dto.AtualizarUsuarioDTO;
import com.UFMSPetSistemas.getpet.controller.usuario.dto.ListarUsuariosDTO;
import com.UFMSPetSistemas.getpet.controller.usuario.dto.PerfilDTO;
import com.UFMSPetSistemas.getpet.model.entities.Usuario;
import com.UFMSPetSistemas.getpet.model.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
@CrossOrigin
public class UsuarioController implements IntUsuarioController {

    @Autowired
    private UsuarioRepository repo;

    @Autowired
    private PasswordEncoder passwordEncoder;

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
            if (usuarios.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
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
            if (usuarios.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
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
    public ResponseEntity<?> putUsuario(final AtualizarUsuarioDTO data, @AuthenticationPrincipal Usuario usuarioLogado) {
        try {
            usuarioLogado.setEmail(data.email());
            usuarioLogado.setRole(data.role());
            usuarioLogado.setNomeCompleto(data.nomeCompleto());
            usuarioLogado.setDataNascimento(data.dataNascimento());
            usuarioLogado.setEndereco(data.endereco());
            usuarioLogado.setCidade(data.cidade());
            usuarioLogado.setUf(data.uf());
            usuarioLogado.setTelefone(data.telefone());

            if (data.profilePicture() != null) {
                usuarioLogado.setFotoPerfil(data.profilePicture());
            }

            if (data.senha() != null && !data.senha().trim().isEmpty()) {
                String encryptedPassword = this.passwordEncoder.encode(data.senha());
                usuarioLogado.setSenha(encryptedPassword);
            }

            this.repo.save(usuarioLogado);
            var PerfilDTO = new PerfilDTO(usuarioLogado);

            return ResponseEntity.ok(PerfilDTO);

        } catch (Exception e) {
            System.err.println("Erro ao atualizar: " + e.getMessage());

            return ResponseEntity.unprocessableEntity().body(e.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> buscarLogado(@AuthenticationPrincipal Usuario usuarioLogado) {
        var PerfilDTO = new PerfilDTO(usuarioLogado);
        return ResponseEntity.ok(PerfilDTO);
    }

    @Override
    public void deleteColaborador(final Long id) {
        this.repo.deleteById(id);
    }
}
