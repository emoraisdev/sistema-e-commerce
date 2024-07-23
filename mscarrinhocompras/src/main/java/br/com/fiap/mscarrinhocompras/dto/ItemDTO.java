package br.com.fiap.mscarrinhocompras.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemDTO {

    @Valid
    @NotNull(message = "Id do item não informado")
    private Long itemId;

    @Valid
    @NotNull(message = "Quantidade do item não informado")
    private int quantidadeItem;
}
