package br.com.fiap.mspagamentos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemCarrinhoDTO {
    private String idItem;
    private String nome;
    private String descricao;
    private Integer quantidade;
    private BigDecimal preco;

}