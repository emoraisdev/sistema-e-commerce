package br.com.fiap.msprecos.service;

import br.com.fiap.msprecos.model.PrecoItem;

public interface PrecoItemService {

    PrecoItem incluir(PrecoItem precoItem);

    PrecoItem findByItemId(Long itemId);

    PrecoItem alterar(PrecoItem precoItem);
}
