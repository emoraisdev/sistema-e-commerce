package br.com.fiap.msprecos.repository;

import br.com.fiap.msprecos.model.PrecoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PrecoItemRepository extends JpaRepository<PrecoItem, Long> {

    Optional<PrecoItem> findPrecoItemByItemId(Long itemId);
}
