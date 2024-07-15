package br.com.fiap.mspagamentos.controller;

import br.com.fiap.mspagamentos.dto.PagamentoDTO;
import br.com.fiap.mspagamentos.dto.ResumoPagamentoDTO;
import br.com.fiap.mspagamentos.model.Pagamento;
import br.com.fiap.mspagamentos.model.PagamentoEnum;
import br.com.fiap.mspagamentos.service.PagamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pagamento")
public class PagamentoController {

    @Autowired
    private PagamentoService pagamentoService;

    @PostMapping("/realizar/{idCarrinho}")
    public ResponseEntity<ResumoPagamentoDTO> realizarPagamento(@RequestBody PagamentoDTO pagamentoDTO, @PathVariable Long idCarrinho) {
        Pagamento pagamento = new Pagamento();
        pagamento.setUsuarioId(pagamentoDTO.getUsuarioId());
        pagamento.setMetodoPagamento(pagamentoDTO.getMetodoPagamento());

        ResumoPagamentoDTO resumoPagamentoDTO = pagamentoService.realizarPagamento(pagamento, idCarrinho);
        return new ResponseEntity<>(resumoPagamentoDTO, HttpStatus.CREATED);
    }

    @PostMapping("/confirmar/{pagamentoId}")
    public ResponseEntity<Pagamento> confirmarPagamento(@PathVariable Long pagamentoId, @RequestParam PagamentoEnum status) {
        Pagamento pagamento = pagamentoService.confirmarPagamento(pagamentoId, status);
        return new ResponseEntity<>(pagamento, HttpStatus.OK);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}