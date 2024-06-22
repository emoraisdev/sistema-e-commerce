package br.com.fiap.mslogin.controller;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsuarioDTO(
        @NotBlank(message = "O nome não pode estar em branco")
        String nome,
        @NotBlank(message = "O e-mail não pode estar em branco")
        @Email(message = "E-mail inválido")
        String email,
        @Size(min = 6, message = "A senha deve ter pelo menos 6 caracteres")
        @Size(max = 12, message = "A senha deve ter no máximo 12 caracteres")
        String senha,

        @Size(min = 6, message = "A senha de confirmação deve ter pelo menos 6 caracteres")
        @Size(max = 12, message = "A senha de confirmação deve ter no máximo 12 caracteres")
        String confirmacaoSenha
) {
}
