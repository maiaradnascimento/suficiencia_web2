package br.edu.utfpr.server.service;

import br.edu.utfpr.server.model.Item;
import br.edu.utfpr.server.repository.ItemRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ItemService {

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public Page<Item> findAll(Pageable pageable) {
        return itemRepository.findAll(pageable);
    }

    public List<Item> findAll() {
        return itemRepository.findAll();
    }

    public Optional<Item> findById(Long id) {
        return itemRepository.findById(id);
    }

    public Item save(Item item) {
        return itemRepository.save(item);
    }

    public Item saveWithImage(Item item, MultipartFile imageFile) throws IOException {
        if (imageFile != null && !imageFile.isEmpty()) {
            item.setImagem(imageFile.getBytes());
            item.setTipoImagem(imageFile.getContentType());
        }
        return itemRepository.save(item);
    }

    public void deleteById(Long id) {
        itemRepository.deleteById(id);
    }

    public Page<Item> search(String searchTerm, Pageable pageable) {
        return itemRepository.findByNomeContainingIgnoreCase(searchTerm, pageable);
    }

    public boolean existsById(Long id) {
        return itemRepository.existsById(id);
    }
}
