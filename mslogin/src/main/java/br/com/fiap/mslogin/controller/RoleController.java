package br.com.fiap.mslogin.controller;

import br.com.fiap.mslogin.controller.dto.UsuarioResponseDTO;
import br.com.fiap.mslogin.model.Role;
import br.com.fiap.mslogin.service.RoleService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Page<Role>> listar(@RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "10") int size) {

        var roles = service.listar(PageRequest.of(page, size));
        return new ResponseEntity<>(roles, HttpStatus.OK);
    }

    @PutMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Role> atualizar(@Valid @RequestBody Role role) {

        var roleAtualizada = service.atualizar(role);

        return new ResponseEntity<>(roleAtualizada, HttpStatus.CREATED);
    }
}
