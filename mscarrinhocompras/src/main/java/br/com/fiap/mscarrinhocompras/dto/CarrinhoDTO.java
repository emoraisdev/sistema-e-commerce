package br.com.fiap.mscarrinhocompras.dto;

import br.com.fiap.mspedidos.model.FormaPagamento;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PedidoDTO {

    @NotNull(message = "Id do cliente não informado")
    private Long clienteId;

    @Valid
    @NotNull(message = "EnderecoEntrega do cliente não informado")
    private EnderecoEntregaDTO enderecoEntrega;

    @Valid
    @NotNull(message = "Itens do pedido não informado")
    @Size(min = 1, message = "Necessário ter ao menos 1 item pedido")
    private List<ItemPedidoDTO> itensPedido;

    @NotNull(message = "forma de pagamento não informado")
    private FormaPagamento formaPagamento;
}
