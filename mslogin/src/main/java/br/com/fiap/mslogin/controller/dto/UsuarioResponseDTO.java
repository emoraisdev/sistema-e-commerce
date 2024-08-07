package br.com.fiap.mslogin.controller.dto;

import java.util.List;

public record UsuarioResponseDTO(
        Long id,
        String email,
        String nome,
        List<String> roles
) {
}
