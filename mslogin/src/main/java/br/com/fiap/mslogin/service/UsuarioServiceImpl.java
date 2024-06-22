package br.com.fiap.mslogin.service;

import br.com.fiap.mslogin.controller.UsuarioDTO;
import br.com.fiap.mslogin.exception.BusinessException;
import br.com.fiap.mslogin.model.Usuario;
import br.com.fiap.mslogin.repository.UsuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UsuarioServiceImpl implements UsuarioService{

    private final UsuarioRepository repo;

    private final PasswordEncoderService passwordEncoder;

    @Override
    public void incluir(UsuarioDTO dto) {

        if (!dto.senha().equals(dto.confirmacaoSenha())) {
            throw new BusinessException("As senhas devem ser iguais.");
        }

        if (repo.getUsuarioByEmail(dto.email()) != null) {
            throw new BusinessException("E-mail já utilizado.");
        }

        Usuario usuario = toEntity(dto);
        usuario.setSenha(passwordEncoder.encodePassword(dto.senha()));

        repo.save(usuario);
    }

    private Usuario toEntity(UsuarioDTO dto){
        return Usuario.builder()
                .nome(dto.nome())
                .email(dto.email())
                .senha(dto.senha())
                .build();
    }
}
