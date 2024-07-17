package br.com.fiap.mspagamentos.dto;

import br.com.fiap.mspagamentos.model.Pagamento;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
public class ResumoPagamentoDTO {
    private Pagamento pagamento;
    private List<ItemCarrinhoDTO> itens;
    private BigDecimal totalPedido;
}