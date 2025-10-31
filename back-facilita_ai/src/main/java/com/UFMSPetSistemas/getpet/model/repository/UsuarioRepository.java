package com.UFMSPetSistemas.getpet.model.repository;


import com.UFMSPetSistemas.getpet.model.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long>{

    UserDetails findByEmail(String email);

    List<Usuario> findByEnderecoContaining(String endereco);

    List<Usuario> findByNomeCompletoContaining(String nomeCompleto);

    void deleteById(@NonNull Long id);
    
}
