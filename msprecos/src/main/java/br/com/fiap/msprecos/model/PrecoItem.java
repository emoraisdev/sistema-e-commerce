package br.com.fiap.msprecos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
public class PrecoItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "O campo Preço é obrigatório.")
    @Column(nullable = false)
    private BigDecimal preco;

    @NotNull(message = "O campo Item Id é obrigatório.")
    @Column(nullable = false, unique = true)
    private Long itemId;

}
