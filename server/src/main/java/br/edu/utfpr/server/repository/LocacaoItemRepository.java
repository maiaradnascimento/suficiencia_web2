package br.edu.utfpr.server.repository;

import br.edu.utfpr.server.model.LocacaoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocacaoItemRepository extends JpaRepository<LocacaoItem, Long> {

    @Query("SELECT li FROM LocacaoItem li WHERE li.locacao.id = :locacaoId")
    List<LocacaoItem> findByLocacaoId(@Param("locacaoId") Long locacaoId);
    
    @Query("SELECT li FROM LocacaoItem li WHERE li.item.id = :itemId")
    List<LocacaoItem> findByItemId(@Param("itemId") Long itemId);
    
    @Query("SELECT li FROM LocacaoItem li WHERE li.locacao.id = :locacaoId AND li.item.id = :itemId")
    Optional<LocacaoItem> findByLocacaoIdAndItemId(@Param("locacaoId") Long locacaoId, @Param("itemId") Long itemId);
    
    @Query("SELECT li FROM LocacaoItem li JOIN FETCH li.item WHERE li.locacao.id = :locacaoId")
    List<LocacaoItem> findByLocacaoIdWithItem(@Param("locacaoId") Long locacaoId);
    
    @Query("SELECT li FROM LocacaoItem li JOIN FETCH li.locacao JOIN FETCH li.item")
    List<LocacaoItem> findAllWithLocacaoAndItem();
    
    boolean existsByLocacaoIdAndItemId(Long locacaoId, Long itemId);
}
