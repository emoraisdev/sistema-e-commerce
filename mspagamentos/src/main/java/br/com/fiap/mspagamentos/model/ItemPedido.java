package br.com.fiap.mscarrinhocompras.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ItemPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private br.com.fiap.mscarrinhocompras.model.Pedido pedido;

    @ManyToOne
    private Item item;

    private int quantidade;

    private BigDecimal preco;
}
