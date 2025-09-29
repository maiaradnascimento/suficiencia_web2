package br.edu.utfpr.server.service;

import br.edu.utfpr.server.dto.LocacaoDTO;
import br.edu.utfpr.server.model.Cliente;
import br.edu.utfpr.server.model.Item;
import br.edu.utfpr.server.model.Locacao;
import br.edu.utfpr.server.model.LocacaoItem;
import br.edu.utfpr.server.repository.ClienteRepository;
import br.edu.utfpr.server.repository.ItemRepository;
import br.edu.utfpr.server.repository.LocacaoItemRepository;
import br.edu.utfpr.server.repository.LocacaoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class LocacaoService {

    private final LocacaoRepository locacaoRepository;
    private final ClienteRepository clienteRepository;
    private final ItemRepository itemRepository;
    private final LocacaoItemRepository locacaoItemRepository;

    public LocacaoService(LocacaoRepository locacaoRepository, 
                         ClienteRepository clienteRepository,
                         ItemRepository itemRepository,
                         LocacaoItemRepository locacaoItemRepository) {
        this.locacaoRepository = locacaoRepository;
        this.clienteRepository = clienteRepository;
        this.itemRepository = itemRepository;
        this.locacaoItemRepository = locacaoItemRepository;
    }

    public Page<Locacao> findAll(Pageable pageable) {
        return locacaoRepository.findAllWithClienteOrderByDataDesc(pageable);
    }

    public Optional<Locacao> findById(Long id) {
        return locacaoRepository.findById(id);
    }

    public Locacao save(Locacao locacao) {
        return locacaoRepository.save(locacao);
    }

    public void deleteById(Long id) {
        locacaoRepository.deleteById(id);
    }

    public Page<Locacao> search(String searchTerm, Pageable pageable) {
        return locacaoRepository.findByClienteNomeContainingIgnoreCaseOrNumnotaContainingIgnoreCase(
                searchTerm, searchTerm, pageable);
    }

    @Transactional
    public Locacao createLocacao(LocacaoDTO locacaoDTO) {
    
        Cliente cliente = clienteRepository.findById(locacaoDTO.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        Locacao locacao = new Locacao();
        locacao.setNumnota(locacaoDTO.getNumnota());
        locacao.setData(locacaoDTO.getData());
        locacao.setCliente(cliente);
        
        Locacao savedLocacao = locacaoRepository.save(locacao);

 
        if (locacaoDTO.getItens() != null) {
            for (var itemDTO : locacaoDTO.getItens()) {
                Item item = itemRepository.findById(itemDTO.getItemId())
                        .orElseThrow(() -> new RuntimeException("Item não encontrado"));


                Optional<LocacaoItem> existingItem = locacaoItemRepository
                        .findByLocacaoIdAndItemId(savedLocacao.getId(), itemDTO.getItemId());

                if (existingItem.isPresent()) {
        
                    LocacaoItem existing = existingItem.get();
                    existing.setQuantidade(existing.getQuantidade() + itemDTO.getQuantidade());
                    existing.setValor(existing.getValor().add(itemDTO.getValor()));
                    locacaoItemRepository.save(existing);
                } else {
                  
                    LocacaoItem locacaoItem = new LocacaoItem();
                    locacaoItem.setLocacao(savedLocacao);
                    locacaoItem.setItem(item);
                    locacaoItem.setQuantidade(itemDTO.getQuantidade());
                    locacaoItem.setValor(itemDTO.getValor());

                    locacaoItemRepository.save(locacaoItem);
                }
            }
        }

        return savedLocacao;
    }
}
