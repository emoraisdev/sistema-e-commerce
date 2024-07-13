package br.com.fiap.mspagamentos.service;

import br.com.fiap.mspagamentos.dto.CarrinhoDTO;
import br.com.fiap.mspagamentos.dto.ItemCarrinhoDTO;
import br.com.fiap.mspagamentos.dto.ResumoPagamentoDTO;
import br.com.fiap.mspagamentos.model.Pagamento;
import br.com.fiap.mspagamentos.model.PagamentoEnum;
import br.com.fiap.mspagamentos.repository.PagamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PagamentoService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private PagamentoRepository pagamentoRepository;

    @Value("${api.msitem.url}")
    private String msItemUrl;

    public ResumoPagamentoDTO realizarPagamento(Pagamento pagamento, Long idCarrinho) {
        var carrinho = restTemplate.getForObject(msItemUrl, CarrinhoDTO.class, idCarrinho);

        if (carrinho == null) {
            throw new IllegalArgumentException("Carrinho inválido");
        }
        var total = calcularTotalPedido(carrinho.getItens());
        carrinho.setTotalPedido(total);

        pagamento.setValor(total.doubleValue());
        pagamentoRepository.save(pagamento);

        return new ResumoPagamentoDTO(pagamento, carrinho.getItens(), carrinho.getTotalPedido());
    }

    private BigDecimal calcularTotalPedido(List<ItemCarrinhoDTO> itens) {
        var total = BigDecimal.ZERO;
        for (ItemCarrinhoDTO item : itens) {
            BigDecimal itemTotal = item.getPreco().multiply(BigDecimal.valueOf(item.getQuantidade()));
            total = total.add(itemTotal);
        }
        return total;
    }
}