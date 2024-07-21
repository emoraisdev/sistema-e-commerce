package br.com.fiap.mslogin.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UsuarioUpdateDTO(
        @NotNull(message = "O Id deve ser informado.")
        Long id,
        @NotBlank(message = "O nome não pode estar em branco")
        String nome
) {
}
