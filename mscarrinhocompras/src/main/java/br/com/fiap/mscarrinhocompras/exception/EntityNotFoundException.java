package br.com.fiap.mscarrinhocompras.exception;

public class EntityNotFoundException extends RuntimeException{

    public EntityNotFoundException(String entidade) {
        super("%s não encontrado para usuario logado.".formatted(entidade));
    }
}
