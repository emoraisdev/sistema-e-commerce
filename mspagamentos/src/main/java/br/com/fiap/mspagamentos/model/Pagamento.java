package br.com.fiap.mspagamentos.model;

import jakarta.persistence.*;
import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
@Entity
public class Pagamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long usuarioId;
    private Double valor;
    @Enumerated(EnumType.STRING)
    private PagamentoEnum status;
    private String metodoPagamento;
    private LocalDateTime dataPagamento;
}
