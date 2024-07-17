package br.com.fiap.mspagamentos.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;



@Data
public class CarrinhoDTO {
    private Long id;
    private Long usuarioId;
    private List<ItemCarrinhoDTO> itens;
    private BigDecimal totalPedido;
}