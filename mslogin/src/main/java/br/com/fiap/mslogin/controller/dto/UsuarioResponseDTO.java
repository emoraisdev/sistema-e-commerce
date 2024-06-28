package br.com.fiap.mslogin.controller.dto;

public record UsuarioResponseDTO(
        Long id,
        String email,
        String nome
) {
}
