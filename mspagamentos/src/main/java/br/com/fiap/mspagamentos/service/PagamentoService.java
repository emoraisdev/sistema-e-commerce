package br.com.fiap.mspagamentos.service;

import br.com.fiap.mspagamentos.dto.CarrinhoDTO;
import br.com.fiap.mspagamentos.dto.ItemCarrinhoDTO;
import br.com.fiap.mspagamentos.dto.ResumoPagamentoDTO;
import br.com.fiap.mspagamentos.exception.EntityNotFoundException;
import br.com.fiap.mspagamentos.model.Pagamento;
import br.com.fiap.mspagamentos.model.PagamentoEnum;
import br.com.fiap.mspagamentos.repository.PagamentoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


@Service
public class PagamentoService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private PagamentoRepository pagamentoRepository;

    @Value("${api.mscarrinhocompras.url}")
    private String URL_MSCARRINHO;

    @Transactional
    public ResumoPagamentoDTO realizarPagamento(Pagamento pagamento, Long idCarrinho) {
        CarrinhoDTO carrinho = restTemplate.getForObject(URL_MSCARRINHO, CarrinhoDTO.class, idCarrinho);

        if (carrinho == null) {
            throw new IllegalArgumentException("Carrinho inválido");
        }
        var totalPedido = calcularTotalPedido(carrinho.getItens());
        pagamento.setValor(totalPedido.doubleValue());
        pagamento.setStatus(PagamentoEnum.PENDENTE);
        pagamentoRepository.save(pagamento);

        return new ResumoPagamentoDTO(pagamento, carrinho.getItens(), totalPedido);
    }

    private BigDecimal calcularTotalPedido(List<ItemCarrinhoDTO> itens) {
        var total = BigDecimal.ZERO;
        for (ItemCarrinhoDTO item : itens) {
            var itemTotal = item.getPreco().multiply(BigDecimal.valueOf(item.getQuantidade()));
            total = total.add(itemTotal);
        }
        return total;
    }

    @Transactional
    public Pagamento confirmarPagamento(Long pagamentoId, PagamentoEnum status) {
        var pagamento = pagamentoRepository.findById(pagamentoId)
                .orElseThrow(() -> new EntityNotFoundException("Pagamento não encontrado"));

        pagamento.setStatus(status);
        pagamento.setDataPagamento(status == PagamentoEnum.APROVADO ? LocalDateTime.now() : null);
        return pagamentoRepository.save(pagamento);
    }

}