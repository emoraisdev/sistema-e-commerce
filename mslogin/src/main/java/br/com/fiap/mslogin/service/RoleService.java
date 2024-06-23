package br.com.fiap.mslogin.service;

import br.com.fiap.mslogin.model.Role;
import org.springframework.stereotype.Service;

public interface RoleService {

    void incluir(Role role);

    Role findByName(String roleName);
}
