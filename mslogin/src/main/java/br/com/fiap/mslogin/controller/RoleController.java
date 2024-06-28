package br.com.fiap.mslogin.controller;

import br.com.fiap.mslogin.model.Role;
import br.com.fiap.mslogin.service.RoleService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/roles")
@AllArgsConstructor
public class RoleController {

    private final RoleService service;

    @PostMapping
    public ResponseEntity<String> incluir(@Valid @RequestBody Role role){

        service.incluir(role);

        return new ResponseEntity<>("Role criada com sucesso!", HttpStatus.CREATED);
    }
}
