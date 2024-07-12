package br.com.fiap.mscarrinhocompras.service;

import br.com.fiap.mscarrinhocompras.dto.CarrinhoDTO;
import br.com.fiap.mscarrinhocompras.dto.ItemDTO;
import br.com.fiap.mscarrinhocompras.dto.ItemEstoqueResponseDTO;
import br.com.fiap.mscarrinhocompras.exception.BusinessException;
import br.com.fiap.mscarrinhocompras.exception.EntityNotFoundException;
import br.com.fiap.mscarrinhocompras.model.Carrinho;
import br.com.fiap.mscarrinhocompras.model.Item;
import br.com.fiap.mscarrinhocompras.repository.CarrinhoRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CarrinhoService {

    private final CarrinhoRepository carrinhoRepository;

    private final RestTemplate restTemplate;

    private final ObjectMapper objectMapper;
    @Value("${api.msitem.url}")
    private String URL_MSITEMS;

    @Autowired
    public CarrinhoService(CarrinhoRepository carrinhoRepository, RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.carrinhoRepository = carrinhoRepository;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public CarrinhoDTO adicionar(Long usuarioId, List<ItemDTO> itemIdsParaRemover) {

        Carrinho carrinhoExistente = getCarrinhoByUser(usuarioId);
        List<Item> itensList =  getListaItensDisposniveis(itemIdsParaRemover);

        if(carrinhoExistente == null){
            Carrinho carrinho = criarCarrinho(usuarioId, itensList);
            return criarCarrinhoDTO(carrinhoRepository.save(carrinho));
        }else if (carrinhoExistente != null){
            updateItensExistentes(carrinhoExistente, itensList);
            return criarCarrinhoDTO(carrinhoRepository.save(carrinhoExistente));
        }
        return null;
    }

    public CarrinhoDTO remover(Long usuarioId, List<Long> itemIdsParaRemover) {

        Carrinho carrinho = getCarrinhoByUser(usuarioId);

        if(carrinho == null){
            throw new RuntimeException("Carrinho não encontrado");
        }else {
            for (Long itemRemover : itemIdsParaRemover) {
                Iterator<Item> iterator = carrinho.getListItens().iterator();
                while (iterator.hasNext()) {
                    Item item = iterator.next();
                    if (item.getItemId().equals(itemRemover)) {
                        //TODO: Atualiza no serviço de item de acordo com a quantidade, se necessário
                        iterator.remove();
                    }
                }
            }
            return criarCarrinhoDTO(carrinhoRepository.save(carrinho));
        }
    }

    private void updateItensExistentes(Carrinho carrinhoExistente, List<Item> itensList){
        for (Item novoItem : itensList) {
            boolean encontrado = false;
            for (Item item : carrinhoExistente.getListItens()) {
                if (novoItem.getItemId() == item.getItemId() && novoItem.getQuantidadeItem() != item.getQuantidadeItem()){
                    item.setQuantidadeItem(novoItem.getQuantidadeItem());
                    encontrado = true;
                    break;
                }else if(novoItem.getItemId() == item.getItemId() && novoItem.getQuantidadeItem() == item.getQuantidadeItem()){
                    encontrado = true;
                    break;
                }
            }
            if(!encontrado){
                carrinhoExistente.getListItens().add(novoItem);
            }
        }
    }

    private Carrinho getCarrinhoByUser(Long usuarioLogadoId){
        return carrinhoRepository.findByUsuarioLogadoId(usuarioLogadoId)
                .orElseThrow(() -> new EntityNotFoundException(Carrinho.class.getSimpleName()));
    }

    private List<Item> getListaItensDisposniveis(List<ItemDTO> listItens){
        List<Item> itensAvaiableList = new ArrayList<>();
        for (ItemDTO itemDTO : listItens) {
            ResponseEntity<String> response = restTemplate.getForEntity(URL_MSITEMS + "/itens/"+itemDTO.getItemId(), String.class);
            if(response.getStatusCode() == HttpStatus.BAD_REQUEST ||  response.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR){
              throw new RuntimeException("Erro ao verificar item: " + response.getBody());
            } else if(response.getStatusCode() == HttpStatus.OK){
                if(response.getStatusCode() == HttpStatus.OK){
                    try {
                        if(response.getBody() != null){
                            int quantidadeEmEstoque = objectMapper.readValue(response.getBody(), JsonNode.class).get("quantidadeEmEstoque").asInt();

                            if(quantidadeEmEstoque >= itemDTO.getQuantidadeItem()){
                                itensAvaiableList.add(new Item(itemDTO.getItemId(), itemDTO.getQuantidadeItem()));
                            } else{
                                throw new BusinessException(String.format("Quantidade informada do itemId %s indisponivel em estoque", itemDTO.getItemId()));
                            }
                        }else{
                            //TODO produto não encontrado exception
                        }

                    } catch (JsonProcessingException | RuntimeException e) {
                        throw new RuntimeException("Erro ao tratar produtos do estoque", e);
                    }

                } else if(response.getStatusCode() == HttpStatus.BAD_REQUEST){
                    //TODO throw new RegraNegocioException(reponse.getMessage());
                } else if(response.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR){
                    throw new RuntimeException("Erro ao atualizar estoque: " + response.getBody());
                }
            }

        }
        return itensAvaiableList;
    }

    //TODO:Verificar se necessario remover
    private String getListIdItensConcat(List<ItemDTO> itensLista){
        return itensLista.stream()
                .map(item -> String.valueOf(item.getItemId()))
                .collect(Collectors.joining(","));
    }
    private Carrinho criarCarrinho(Long usuarioId, List<Item> itensList) {
        return Carrinho.builder()
                .usuarioLogadoId(usuarioId)
                .listItens(itensList)
                .build();
    }

    private CarrinhoDTO criarCarrinhoDTO(Carrinho carrinho) {
        return CarrinhoDTO.builder()
                .usuarioLogadoId(carrinho.getUsuarioLogadoId())
                .itensLista(getListItensDTO(carrinho.getListItens()))
                .build();
    }

    private List<ItemDTO> getListItensDTO(List<Item> listaItens){
        return listaItens.stream()
                .map(item -> ItemDTO.builder()
                        .itemId(item.getItemId())
                        .quantidadeItem(item.getQuantidadeItem())
                        .build())
                .collect(Collectors.toList());
    }

    public CarrinhoDTO buscarCarrinho(Long userId) {
        return criarCarrinhoDTO(getCarrinhoByUser(userId));
    }
}
