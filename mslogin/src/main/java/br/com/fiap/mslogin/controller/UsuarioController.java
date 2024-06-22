package br.com.fiap.mslogin.controller;

import br.com.fiap.mslogin.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuarios")
@AllArgsConstructor
public class UsuarioController {


    private final UsuarioService service;

    @PostMapping
    public ResponseEntity<String> incluir(@Valid @RequestBody UsuarioDTO dto){

        service.incluir(dto);

        return new ResponseEntity<>("Usuário criado com sucesso!", HttpStatus.CREATED);
    }
}
