package br.com.fiap.msprecos.exception;

public class EntityNotFoundException extends RuntimeException{

    public EntityNotFoundException(String entidade) {
        super("Entidade %s não encontrada.".formatted(entidade));
    }
}
