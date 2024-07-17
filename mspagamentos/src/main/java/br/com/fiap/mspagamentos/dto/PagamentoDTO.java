package br.com.fiap.mspagamentos.dto;

import br.com.fiap.mspagamentos.model.PagamentoEnum;
import lombok.Data;

@Data
public class PagamentoDTO {
    private Long id;
    private Long usuarioId;
    private Double valor;
    private PagamentoEnum status;
    private String metodoPagamento;
}