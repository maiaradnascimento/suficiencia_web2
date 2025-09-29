package br.edu.utfpr.server.repository;

import br.edu.utfpr.server.model.Locacao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface LocacaoRepository extends JpaRepository<Locacao, Long> {

    Optional<Locacao> findByNumnota(String numnota);
    
    boolean existsByNumnota(String numnota);
    
    @Query("SELECT l FROM Locacao l WHERE l.cliente.id = :clienteId")
    List<Locacao> findByClienteId(@Param("clienteId") Long clienteId);
    
    @Query("SELECT l FROM Locacao l WHERE l.data BETWEEN :dataInicio AND :dataFim")
    List<Locacao> findByDataBetween(@Param("dataInicio") LocalDate dataInicio, @Param("dataFim") LocalDate dataFim);
    
    @Query("SELECT l FROM Locacao l WHERE l.data = :data")
    List<Locacao> findByData(@Param("data") LocalDate data);
    
    @Query("SELECT l FROM Locacao l ORDER BY l.data DESC")
    List<Locacao> findAllOrderByDataDesc();
    
    @Query("SELECT l FROM Locacao l JOIN FETCH l.cliente ORDER BY l.data DESC")
    List<Locacao> findAllWithClienteOrderByDataDesc();
    
    @Query("SELECT l FROM Locacao l JOIN FETCH l.cliente ORDER BY l.data DESC")
    Page<Locacao> findAllWithClienteOrderByDataDesc(Pageable pageable);
    
    Page<Locacao> findByClienteNomeContainingIgnoreCaseOrNumnotaContainingIgnoreCase(
            String clienteNome, String numnota, Pageable pageable);
}
