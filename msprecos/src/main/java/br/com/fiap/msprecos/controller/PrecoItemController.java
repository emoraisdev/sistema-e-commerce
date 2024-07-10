package br.com.fiap.msprecos.controller;

import br.com.fiap.msprecos.model.PrecoItem;
import br.com.fiap.msprecos.service.PrecoItemService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/precos")
@AllArgsConstructor
public class PrecoItemController {

    private final PrecoItemService service;

    @PostMapping
    public ResponseEntity<PrecoItem> incluir(@Valid @RequestBody PrecoItem precoItem){

        var precoItemSalvo = service.incluir(precoItem);

        return new ResponseEntity<>(precoItemSalvo, HttpStatus.CREATED);
    }

    @GetMapping(path = "/by-item-id/{itemId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<PrecoItem> getByEmail(@PathVariable Long itemId) {

        var precoItem = service.findByItemId(itemId);
        return new ResponseEntity<>(precoItem, HttpStatus.OK);
    }
}
