package br.com.fiap.mspagamentos.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ItemDTO {
    private Long itemId;
    private String nome;
    private BigDecimal preco;
    private int quantidadeItem;
}