package br.com.fiap.mslogin.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginDTO(
        @NotBlank
        String email,
        @NotBlank
        String senha
) {
}
