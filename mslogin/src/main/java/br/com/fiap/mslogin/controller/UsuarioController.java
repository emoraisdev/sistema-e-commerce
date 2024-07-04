package br.com.fiap.mslogin.controller;

import br.com.fiap.mslogin.controller.dto.LoginDTO;
import br.com.fiap.mslogin.controller.dto.TokenDTO;
import br.com.fiap.mslogin.controller.dto.UsuarioDTO;
import br.com.fiap.mslogin.controller.dto.UsuarioResponseDTO;
import br.com.fiap.mslogin.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
@AllArgsConstructor
public class UsuarioController {


    private final UsuarioService service;

    @PostMapping
    public ResponseEntity<String> incluir(@Valid @RequestBody UsuarioDTO dto){

        service.incluir(dto);

        return new ResponseEntity<>("Usuário criado com sucesso!", HttpStatus.CREATED);
    }

    @PostMapping("/logar")
    public ResponseEntity<TokenDTO> incluir(@Valid @RequestBody LoginDTO dto){

        var token = service.logar(dto);

        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Page<UsuarioResponseDTO>> listar(@RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "10") int size) {

        var usuarios = service.listar(PageRequest.of(page, size));
        return new ResponseEntity<>(usuarios, HttpStatus.OK);
    }

    @GetMapping(path = "/by-email/{email}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<UsuarioResponseDTO> getByEmail(@PathVariable String email) {

        var usuario = service.findByEmail(email);
        return new ResponseEntity<>(usuario, HttpStatus.OK);
    }
}
