package br.com.fiap.mslogin.service;

import br.com.fiap.mslogin.controller.dto.LoginDTO;
import br.com.fiap.mslogin.controller.dto.TokenDTO;
import br.com.fiap.mslogin.controller.dto.UsuarioDTO;
import br.com.fiap.mslogin.model.Usuario;

public interface UsuarioService {

    void incluir(UsuarioDTO dto);

    Usuario findByEmail(String email);

    TokenDTO logar(LoginDTO login);
}
