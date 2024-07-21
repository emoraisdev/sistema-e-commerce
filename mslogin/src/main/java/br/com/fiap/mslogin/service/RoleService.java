package br.com.fiap.mslogin.service;

import br.com.fiap.mslogin.model.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface RoleService {

    void incluir(Role role);

    Role findByName(String roleName);

    Page<Role> listar(PageRequest pageRequest);

    Role atualizar(Role role);
}
