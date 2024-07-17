package br.com.fiap.mscarrinhocompras.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Usuario usuario;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "pedido")
    private List<ItemPedido> itens = new ArrayList<>();

    private LocalDateTime dataPedido;

    private BigDecimal total;
}
