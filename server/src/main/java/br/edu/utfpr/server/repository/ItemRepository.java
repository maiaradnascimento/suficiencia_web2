package br.edu.utfpr.server.repository;

import br.edu.utfpr.server.model.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("SELECT i FROM Item i WHERE LOWER(i.nome) LIKE LOWER(CONCAT('%', :nome, '%'))")
    List<Item> findByNomeContainingIgnoreCase(@Param("nome") String nome);
    
    Page<Item> findByNomeContainingIgnoreCase(String nome, Pageable pageable);
    
    @Query("SELECT i FROM Item i WHERE i.valor BETWEEN :valorMin AND :valorMax")
    List<Item> findByValorBetween(@Param("valorMin") BigDecimal valorMin, @Param("valorMax") BigDecimal valorMax);
    
    @Query("SELECT i FROM Item i ORDER BY i.nome")
    List<Item> findAllOrderByNome();
    
    @Query("SELECT i FROM Item i ORDER BY i.valor")
    List<Item> findAllOrderByValor();
}
