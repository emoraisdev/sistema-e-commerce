package br.com.fiap.mscarrinhocompras.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CarrinhoDTO {

    @Valid
    @NotNull(message = "Id do Usuario não informado")
    private Long usuarioLogadoId;

    private Long carrinhoId;

    @Valid
    @NotNull(message = "Itens do carrinho não informado")
    @Size(min = 1, message = "Necessário ter ao menos 1 item incluido")
    private List<ItemDTO> itensLista;
}
