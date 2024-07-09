package br.com.fiap.mslogin.repository;

import br.com.fiap.mslogin.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> getUsuarioByEmail(String email);
}
