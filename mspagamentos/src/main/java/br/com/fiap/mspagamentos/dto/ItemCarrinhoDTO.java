package br.com.fiap.mspagamentos.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ItemCarrinhoDTO {
    private Long id;
    private String nome;
    private BigDecimal preco;
    private int quantidade;
}