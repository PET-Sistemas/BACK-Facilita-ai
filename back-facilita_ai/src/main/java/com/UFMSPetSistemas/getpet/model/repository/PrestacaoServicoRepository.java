package com.UFMSPetSistemas.getpet.model.repository;

import com.UFMSPetSistemas.getpet.model.entities.PrestacaoServico;
import com.UFMSPetSistemas.getpet.model.entities.Servico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PrestacaoServicoRepository extends JpaRepository<PrestacaoServico, Long> {

    @Query("SELECT p FROM PrestacaoServico p JOIN p.servico s WHERE s.id = :servicoId")
    List<PrestacaoServico> findByServicoId(@Param("servicoId") Long servicoId);

    @Query("SELECT p.servico FROM PrestacaoServico p WHERE p.avaliacao BETWEEN :avaliacaoMinima AND :avaliacaoMaxima")
    List<Servico> findServicosByAvaliacaoBetween(
            @Param("avaliacaoMinima") int avaliacaoMinima,
            @Param("avaliacaoMaxima") int avaliacaoMaxima
    );

    // Consulta personalizada para comparar o ID do usuarioPrestador
    @Query("SELECT p FROM PrestacaoServico p WHERE p.usuarioPrestador.id = :usuarioPrestadorId")
    List<PrestacaoServico> findByUsuarioPrestador(@Param("usuarioPrestadorId") Long usuarioPrestadorId);
}
