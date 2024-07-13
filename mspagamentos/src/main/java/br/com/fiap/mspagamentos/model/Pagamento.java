package br.com.fiap.mspagamentos.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private Long idCarrinho;

    private double valor;

    @Enumerated(EnumType.STRING)
    private PagamentoEnum tipoPagamento;


}