package br.com.fiap.mscarrinhocompras.service;

import br.com.fiap.mscarrinhocompras.dto.CarrinhoDTO;
import br.com.fiap.mscarrinhocompras.dto.ItemDTO;
import br.com.fiap.mscarrinhocompras.dto.ItemEstoqueResponseDTO;
import br.com.fiap.mscarrinhocompras.exception.BusinessException;
import br.com.fiap.mscarrinhocompras.model.Carrinho;
import br.com.fiap.mscarrinhocompras.model.Item;
import br.com.fiap.mscarrinhocompras.repository.CarrinhoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class CarrinhoService {

    private final CarrinhoRepository carrinhoRepository;

    private final RestTemplate restTemplate;
    @Value("${api.msitem.url}")
    private String urlMSItems;

    @Autowired
    public CarrinhoService(CarrinhoRepository carrinhoRepository, RestTemplate restTemplate) {
        this.carrinhoRepository = carrinhoRepository;
        this.restTemplate = restTemplate;
    }

    public CarrinhoDTO adicionar(Long usuarioId, List<ItemDTO> itemIds) {

        Carrinho carrinhoExistente = getCarrinhoByUser(usuarioId);

        if(carrinhoExistente == null){
            validaQuantidadeItensEstoque(itemIds);
            List<Item> itens = listaItensDTOToListItens(itemIds);
            Carrinho carrinho = criarCarrinho(usuarioId, itens);
            return criarCarrinhoDTO(carrinhoRepository.save(carrinho));
        }else if (carrinhoExistente != null){
            validaQuantidadeItensEstoque(itemIds);
            List<Item> itensList = getListaItensAtualizados(itemIds);
            updateItensExistentes(carrinhoExistente, itensList);
            return criarCarrinhoDTO(carrinhoRepository.save(carrinhoExistente));
        }
        return null;
    }

    public CarrinhoDTO remover(Long usuarioId, List<Long> itemIdsParaRemover) {

        Carrinho carrinho = getCarrinhoByUser(usuarioId);

        if(carrinho == null){
            throw new BusinessException("Carrinho não encontrado");
        }else {
            for (Long itemRemover : itemIdsParaRemover) {
                Iterator<Item> iterator = carrinho.getListItens().iterator();
                while (iterator.hasNext()) {
                    Item item = iterator.next();
                    if (item.getItemId().equals(itemRemover)) {
                        atualizaQuantidadeEstoque(item.getItemId(), item.getQuantidadeItem(), true);
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
                if (novoItem.getItemId().equals(item.getItemId())){
                    int novaQuantidade = item.getQuantidadeItem() + novoItem.getQuantidadeItem();
                    item.setQuantidadeItem(novaQuantidade);
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
        return carrinhoRepository.findByUsuarioLogadoId(usuarioLogadoId);
    }

    private void validaQuantidadeItensEstoque(List<ItemDTO> itemIds){
        for (ItemDTO itemDTO : itemIds) {
            ItemEstoqueResponseDTO itemEstoqueAtual = getItemEstoque(itemDTO.getItemId());

            if (itemEstoqueAtual != null && itemEstoqueAtual.getQuantidadeEmEstoque() < itemDTO.getQuantidadeItem()) {
                throw new BusinessException(String.format("Quantidade informada do itemId %s indisponível em estoque", itemDTO.getItemId()));
            }
        }
    }

    private List<Item> getListaItensAtualizados(List<ItemDTO> listItens){
        List<Item> itensList =  new ArrayList<>();
        for (ItemDTO itemDTO : listItens) {
            try {
                atualizaQuantidadeEstoque(itemDTO.getItemId(), itemDTO.getQuantidadeItem(), false);
                itensList.add(Item.builder()
                        .itemId(itemDTO.getItemId())
                        .quantidadeItem(itemDTO.getQuantidadeItem())
                        .build());
            } catch (HttpClientErrorException.NotFound e) {
                throw new BusinessException(String.format("Item ID %s não encontrado.", itemDTO.getItemId()));
            } catch (HttpClientErrorException.BadRequest e) {
                throw new BusinessException("Erro ao verificar item: " + e.getResponseBodyAsString());
            } catch (HttpServerErrorException e) {
                throw new BusinessException("Erro no servidor ao verificar item: " + e.getResponseBodyAsString());
            }
        }

        return itensList;
    }


    private ItemEstoqueResponseDTO getItemEstoque(Long itemId){
        try {
            ResponseEntity<ItemEstoqueResponseDTO> responseEntity = restTemplate.getForEntity(urlMSItems + "/itens/" + itemId, ItemEstoqueResponseDTO.class);
            if (responseEntity.getBody() != null) {
                return responseEntity.getBody();
            }
        } catch (HttpClientErrorException.NotFound e) {
            throw new BusinessException(String.format("Item ID %s não encontrado.", itemId));
        } catch (HttpClientErrorException.BadRequest e) {
            throw new BusinessException("Erro ao verificar item: " + e.getResponseBodyAsString());
        } catch (HttpServerErrorException e) {
            throw new BusinessException("Erro no servidor ao verificar item: " + e.getResponseBodyAsString());
        }

        return null;
    }

    private void atualizaQuantidadeEstoque(Long itemId, int novaQuantidade, boolean isAcrescentar){
        ItemEstoqueResponseDTO itemEstoqueAtual = getItemEstoque(itemId);
        try {
            int quantidadeAtualizada;

            if(itemEstoqueAtual != null){
                if(isAcrescentar){
                    quantidadeAtualizada = itemEstoqueAtual.getQuantidadeEmEstoque() + novaQuantidade;
                }else{
                    quantidadeAtualizada = itemEstoqueAtual.getQuantidadeEmEstoque() - novaQuantidade;
                }
                itemEstoqueAtual.setQuantidadeEmEstoque(quantidadeAtualizada);
            }

            new RestTemplate().put(urlMSItems + "/itens/" + itemId, itemEstoqueAtual);

            } catch (HttpClientErrorException.NotFound e) {
                throw new BusinessException(String.format("Item ID %s não encontrado para update.", itemId));
            } catch (HttpClientErrorException.BadRequest e) {
                throw new BusinessException("Erro ao atualizar item: " + e.getResponseBodyAsString());
            } catch (HttpServerErrorException e) {
                throw new BusinessException("Erro no servidor ao atualizar item: " + e.getResponseBodyAsString());
            }
    }

    private Carrinho criarCarrinho(Long usuarioId, List<Item> itensList) {
        return Carrinho.builder()
                .usuarioLogadoId(usuarioId)
                .listItens(itensList)
                .build();
    }

    private CarrinhoDTO criarCarrinhoDTO(Carrinho carrinho) {
        return CarrinhoDTO.builder()
                .carrinhoId(carrinho.getId())
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
                .toList();
    }

    public CarrinhoDTO buscarCarrinho(Long userId) {
        return criarCarrinhoDTO(getCarrinhoByUser(userId));
    }

    private List<Item> listaItensDTOToListItens(List<ItemDTO> listItens){
        List<Item> itensAvaiableList = new ArrayList<>();
        for (ItemDTO itemDTO : listItens) {
            itensAvaiableList.add(Item.builder()
                    .itemId(itemDTO.getItemId())
                    .quantidadeItem(itemDTO.getQuantidadeItem())
                    .build());
        }
        return itensAvaiableList;
    }
}
