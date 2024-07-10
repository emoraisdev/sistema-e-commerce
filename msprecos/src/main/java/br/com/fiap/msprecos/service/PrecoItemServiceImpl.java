package br.com.fiap.msprecos.service;

import br.com.fiap.msprecos.exception.BusinessException;
import br.com.fiap.msprecos.exception.EntityNotFoundException;
import br.com.fiap.msprecos.model.PrecoItem;
import br.com.fiap.msprecos.repository.PrecoItemRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PrecoItemServiceImpl implements PrecoItemService{

    private final PrecoItemRepository repo;

    @Override
    public PrecoItem incluir(PrecoItem precoItem) {

        if (repo.findPrecoItemByItemId(precoItem.getItemId()).isPresent()) {
            throw new BusinessException("Já existe um preço cadastrado para este produto.");
        }

        return repo.save(precoItem);
    }

    @Override
    public PrecoItem findByItemId(Long itemId) {
        return repo.findPrecoItemByItemId(itemId)
                .orElseThrow(() -> new EntityNotFoundException(PrecoItem.class.getSimpleName()));
    }

    @Override
    public PrecoItem alterar(PrecoItem precoItem) {
        return repo.save(precoItem);
    }
}
