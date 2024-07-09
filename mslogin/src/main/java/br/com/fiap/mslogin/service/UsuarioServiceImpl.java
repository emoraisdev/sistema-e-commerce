package br.com.fiap.mslogin.service;

import br.com.fiap.mslogin.constants.Constants;
import br.com.fiap.mslogin.controller.dto.*;
import br.com.fiap.mslogin.exception.BusinessException;
import br.com.fiap.mslogin.exception.EntityNotFoundException;
import br.com.fiap.mslogin.exception.LoginInvalido;
import br.com.fiap.mslogin.model.Role;
import br.com.fiap.mslogin.model.Usuario;
import br.com.fiap.mslogin.repository.UsuarioRepository;
import br.com.fiap.mslogin.security.TokenService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

        if (repo.getUsuarioByEmail(dto.email()).isPresent()) {
            throw new BusinessException("E-mail já utilizado.");
        }

        Usuario usuario = toEntity(dto);
        usuario.addRole(roleUser);
        usuario.setSenha(passwordEncoder.encodePassword(dto.senha()));

        repo.save(usuario);
    }

    @Override
    public UsuarioResponseDTO findByEmail(String email) {

        var usuario = repo.getUsuarioByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException(Usuario.class.getSimpleName()));

        return toUsuarioResponseDTO(usuario);
    }

    @Override
    public TokenDTO logar(LoginDTO login) {

        var usuario = repo.getUsuarioByEmail(login.email()).orElseThrow(LoginInvalido::new);

        if (passwordEncoder.matches(login.senha(), usuario.getSenha())){
            return new TokenDTO(tokenService.generateToken(usuario));
        } else {
            throw new LoginInvalido();
        }
    }

    @Override
    public Page<UsuarioResponseDTO> listar(PageRequest pageRequest) {
        var usuarios =  repo.findAll(pageRequest);
        return usuarios.map(this::toUsuarioResponseDTO);
    }

    @Override
    public UsuarioResponseDTO atribuirRole(UsuarioRoleDTO usuarioResponseDTO) {

        var usuario = repo.getUsuarioByEmail(usuarioResponseDTO.email())
                .orElseThrow(() -> new BusinessException("Usuário Não encontrado!"));

        var role = roleService.findByName(usuarioResponseDTO.role());

        if (usuario.getRoles().stream().anyMatch( r -> r.getId().equals(role.getId()))) {
           throw new BusinessException("Usuário já possui a Role especificada.");
        }

        usuario.addRole(role);

        return toUsuarioResponseDTO(repo.save(usuario));
    }

    private Usuario toEntity(UsuarioDTO dto){
        return Usuario.builder()
                .nome(dto.nome())
                .email(dto.email())
                .senha(dto.senha())
                .build();
    }

    private UsuarioResponseDTO toUsuarioResponseDTO(Usuario entity){
        return new UsuarioResponseDTO(
                entity.getId(),
                entity.getEmail(),
                entity.getNome(),
                entity.getRoles().stream().map(Role::getName).toList()
        );
    }
}
