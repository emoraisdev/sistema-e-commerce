package br.com.fiap.mslogin.repository;

import br.com.fiap.mslogin.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findRoleByName(String name);
}
