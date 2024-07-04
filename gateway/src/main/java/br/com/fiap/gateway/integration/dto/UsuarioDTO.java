package br.com.fiap.gateway.integration.dto;

import java.util.List;

public record UsuarioDTO(
        Long id,
        String email,
        String nome,
        List<String> roles
) {
}
