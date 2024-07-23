package br.com.fiap.mspagamentos.service;

import br.com.fiap.mspagamentos.dto.CarrinhoDTO;
import br.com.fiap.mspagamentos.dto.ItemDTO;
import br.com.fiap.mspagamentos.dto.ItemEstoqueResponseDTO;
import br.com.fiap.mspagamentos.dto.ResumoPagamentoDTO;
import br.com.fiap.mspagamentos.exception.EntityNotFoundException;
import br.com.fiap.mspagamentos.model.Pagamento;
import br.com.fiap.mspagamentos.model.PagamentoEnum;
import br.com.fiap.mspagamentos.repository.PagamentoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
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

    @Value("${api.msitem.url}")
    private String urlMSItems;

    @Transactional
    public ResumoPagamentoDTO realizarPagamento(Pagamento pagamento, Long idCarrinho) {

        CarrinhoDTO carrinho = getCarrinhoDTO(pagamento, idCarrinho);

        if (carrinho == null || carrinho.getItensLista() == null) {
            throw new IllegalArgumentException("Carrinho inválido");
        }

        carrinho.getItensLista().forEach( item -> {

            var itemIntegracao = getItem(item.getItemId());

            item.setNome(itemIntegracao.getNome());
            item.setPreco(itemIntegracao.getPreco());
        });

        var totalPedido = calcularTotalPedido(carrinho.getItensLista());
        pagamento.setValor(totalPedido.doubleValue());
        pagamento.setStatus(PagamentoEnum.PENDENTE);
        pagamentoRepository.save(pagamento);

        return new ResumoPagamentoDTO(pagamento, carrinho.getItensLista(), totalPedido);
    }

    private CarrinhoDTO getCarrinhoDTO(Pagamento pagamento, Long idCarrinho) {
        // Cria o objeto HttpHeaders e adiciona o cabeçalho
        HttpHeaders headers = new HttpHeaders();
        headers.set("usuarioId", pagamento.getUsuarioId().toString()); // Adicione seu cabeçalho necessário aqui

        // Cria o HttpEntity com os cabeçalhos
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        // Usa o método exchange para enviar a requisição com cabeçalhos
        ResponseEntity<CarrinhoDTO> response = restTemplate.exchange(
                URL_MSCARRINHO,
                HttpMethod.GET,       // Método HTTP (GET)
                entity,               // Entidade contendo os cabeçalhos
                CarrinhoDTO.class     // Tipo de resposta esperada
        );

        // Obtém o corpo da resposta
        return response.getBody();
    }

    private ItemEstoqueResponseDTO getItem(Long itemId){

        ResponseEntity<ItemEstoqueResponseDTO> responseEntity = restTemplate.getForEntity(urlMSItems + "/itens/" + itemId, ItemEstoqueResponseDTO.class);
        if (responseEntity.getBody() != null) {
            return responseEntity.getBody();
        }

        return null;
    }

    private BigDecimal calcularTotalPedido(List<ItemDTO> itens) {
        var total = BigDecimal.ZERO;
        for (ItemDTO item : itens) {
            var itemTotal = item.getPreco().multiply(BigDecimal.valueOf(item.getQuantidadeItem()));
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