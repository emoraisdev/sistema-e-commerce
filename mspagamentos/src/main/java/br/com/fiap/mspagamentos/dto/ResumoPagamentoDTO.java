package br.com.fiap.mspagamentos.dto;

import br.com.fiap.mspagamentos.model.Pagamento;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
public class ResumoPagamentoDTO {
    private Pagamento pagamento;
    private List<ItemDTO> itens;
    private BigDecimal totalPedido;
}