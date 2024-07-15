package br.com.fiap.mscarrinhocompras.repository;

import br.com.fiap.mscarrinhocompras.model.Carrinho;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CarrinhoRepository extends JpaRepository<Carrinho, String> {

    Carrinho findByUsuarioLogadoId(Long usuarioLogadoId);
}
