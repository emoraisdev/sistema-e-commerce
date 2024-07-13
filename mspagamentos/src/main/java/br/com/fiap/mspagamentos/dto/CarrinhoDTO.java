package br.com.fiap.mspagamentos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarrinhoDTO {
    private Long idCarrinho;
    private List<ItemCarrinhoDTO> itens;
    private BigDecimal totalPedido;
}