package br.com.fiap.mslogin.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record UsuarioRoleDTO(
        @NotBlank
        String email,
        @NotBlank
        String role
) {
}
