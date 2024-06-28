package br.com.fiap.mslogin.service;

import br.com.fiap.mslogin.controller.dto.LoginDTO;
import br.com.fiap.mslogin.controller.dto.TokenDTO;
import br.com.fiap.mslogin.controller.dto.UsuarioDTO;
import br.com.fiap.mslogin.controller.dto.UsuarioResponseDTO;
import br.com.fiap.mslogin.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface UsuarioService {

    void incluir(UsuarioDTO dto);

    Usuario findByEmail(String email);

    TokenDTO logar(LoginDTO login);

    Page<UsuarioResponseDTO> listar(PageRequest pageRequest);
}
