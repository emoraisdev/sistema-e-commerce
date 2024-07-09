package br.com.fiap.mslogin.service;

import br.com.fiap.mslogin.exception.BusinessException;
import br.com.fiap.mslogin.exception.EntityNotFoundException;
import br.com.fiap.mslogin.model.Role;
import br.com.fiap.mslogin.repository.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class RoleServiceImpl implements RoleService{

    private final RoleRepository repo;

    @Override
    public void incluir(Role role) {

        if (repo.findRoleByName(role.getName()).isPresent()) {
            throw new BusinessException("Role já cadastrada!");
        }

        repo.save(role);
    }

    @Override
    public Role findByName(String roleName) {
        return repo.findRoleByName(roleName)
                .orElseThrow(() -> new EntityNotFoundException(Role.class.getSimpleName()));
    }

    @Override
    public Page<Role> listar(PageRequest pageRequest) {
        return repo.findAll(pageRequest);
    }
}
