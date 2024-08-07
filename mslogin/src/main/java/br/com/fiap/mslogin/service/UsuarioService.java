package br.com.fiap.mslogin.service;

import br.com.fiap.mslogin.controller.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface UsuarioService {

    void incluir(UsuarioDTO dto);

    UsuarioResponseDTO findByEmail(String email);

    TokenDTO logar(LoginDTO login);

    Page<UsuarioResponseDTO> listar(PageRequest pageRequest);

    UsuarioResponseDTO atribuirRole(UsuarioRoleDTO usuarioResponseDTO);

    UsuarioResponseDTO removerRole(UsuarioRoleDTO usuarioResponseDTO);

    UsuarioResponseDTO atualizar(Long usuarioId, UsuarioUpdateDTO dto);
}
