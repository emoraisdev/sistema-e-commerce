package br.com.fiap.mscarrinhocompras.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long Id;

    @Column(nullable = false)
    private Long itemId;

    @Column(nullable = false)
    private Integer quantidadeItem;

    public Item(Long itemId, Integer quantidadeItem){
        this.itemId = itemId;
        this.quantidadeItem = quantidadeItem;
    }
}
