package br.com.fiap.mspagamentos.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;



@Data
public class CarrinhoDTO {
    private Long carrinhoId;
    private Long usuarioLogadoId;
    private List<ItemDTO> itensLista;
    private BigDecimal totalPedido;
}