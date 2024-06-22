package br.com.fiap.mslogin.repository;

import br.com.fiap.mslogin.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    public Usuario getUsuarioByEmail(String email);
}
