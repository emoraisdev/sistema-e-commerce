package br.com.fiap.mspagamentos.controller;

import br.com.fiap.mspagamentos.dto.ResumoPagamentoDTO;
import br.com.fiap.mspagamentos.model.Pagamento;
import br.com.fiap.mspagamentos.service.PagamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class PagamentoController {

    @Autowired
    private PagamentoService pagamentoService;

    public PagamentoController(PagamentoService pagamentoService) {
        this.pagamentoService = pagamentoService;
    }

    @PostMapping("/pagamento")
    public ResumoPagamentoDTO realizarPagamento(@RequestBody Pagamento pagamento) {
        return pagamentoService.realizarPagamento(pagamento, pagamento.getIdCarrinho());
    }

}