package br.com.fiap.mscarrinhocompras.controller;


import br.com.fiap.mscarrinhocompras.dto.CarrinhoDTO;
import br.com.fiap.mscarrinhocompras.dto.ItemDTO;
import br.com.fiap.mscarrinhocompras.service.CarrinhoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/carrinho")
public class CarrinhoController {

    private final CarrinhoService carrinhoService;

    @Autowired
    public CarrinhoController(CarrinhoService carrinhoService) {
        this.carrinhoService = carrinhoService;
    }

    @PostMapping()
    public ResponseEntity<CarrinhoDTO> adicionar(@RequestHeader("usuarioId") Long usuarioId, @RequestBody List<ItemDTO> itemIdsParaRemover) {
        return ResponseEntity.ok(carrinhoService.adicionar(usuarioId, itemIdsParaRemover));
    }

    @DeleteMapping()
    public ResponseEntity<CarrinhoDTO> remover(@RequestHeader("usuarioId") Long usuarioId, @RequestBody List<Long> itemIdsParaRemover) {
        return ResponseEntity.ok(carrinhoService.remover(usuarioId, itemIdsParaRemover));
    }

    @GetMapping()
    public ResponseEntity<CarrinhoDTO> visualizar(@RequestHeader("usuarioId") Long usuarioId) {
        return ResponseEntity.ok(carrinhoService.buscarCarrinho(usuarioId));
    }
}
