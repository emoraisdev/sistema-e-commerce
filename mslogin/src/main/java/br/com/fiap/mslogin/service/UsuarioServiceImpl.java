package br.com.fiap.mslogin.service;

import br.com.fiap.mslogin.constants.Constants;
import br.com.fiap.mslogin.controller.dto.LoginDTO;
import br.com.fiap.mslogin.controller.dto.TokenDTO;
import br.com.fiap.mslogin.controller.dto.UsuarioDTO;
import br.com.fiap.mslogin.exception.BusinessException;
import br.com.fiap.mslogin.exception.LoginInvalido;
import br.com.fiap.mslogin.model.Usuario;
import br.com.fiap.mslogin.repository.UsuarioRepository;
import br.com.fiap.mslogin.security.TokenService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UsuarioServiceImpl implements UsuarioService{

    private final UsuarioRepository repo;

    private final RoleService roleService;

    private final PasswordEncoderService passwordEncoder;

    private final TokenService tokenService;

    @Override
    public void incluir(UsuarioDTO dto) {

        var roleUser = roleService.findByName(Constants.ROLE_USER);

        if (roleUser == null) {
            throw new BusinessException("Role USER não cadastrada.");
        }

        if (!dto.senha().equals(dto.confirmacaoSenha())) {
            throw new BusinessException("As senhas devem ser iguais.");
        }

        if (repo.getUsuarioByEmail(dto.email()) != null) {
            throw new BusinessException("E-mail já utilizado.");
        }

        Usuario usuario = toEntity(dto);
        // Usuário cadastrado com a role padrão USER;
        usuario.addRole(roleUser);
        usuario.setSenha(passwordEncoder.encodePassword(dto.senha()));

        repo.save(usuario);
    }

    @Override
    public Usuario findByEmail(String email) {
        return repo.getUsuarioByEmail(email);
    }

    @Override
    public TokenDTO logar(LoginDTO login) {

        var usuario = repo.getUsuarioByEmail(login.email());

        if (usuario == null) {
            throw new LoginInvalido();
        }

        if (passwordEncoder.matches(login.senha(), usuario.getPassword())){
            return new TokenDTO(tokenService.generateToken(usuario));
        } else {
            throw new LoginInvalido();
        }
    }

    private Usuario toEntity(UsuarioDTO dto){
        return Usuario.builder()
                .nome(dto.nome())
                .email(dto.email())
                .senha(dto.senha())
                .build();
    }
}
