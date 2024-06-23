package br.com.fiap.mslogin.service;

import br.com.fiap.mslogin.model.Role;
import br.com.fiap.mslogin.repository.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RoleServiceImpl implements RoleService{

    private final RoleRepository repo;

    @Override
    public void incluir(Role role) {
        repo.save(role);
    }

    @Override
    public Role findByName(String roleName) {
        return repo.findRoleByName(roleName);
    }
}
