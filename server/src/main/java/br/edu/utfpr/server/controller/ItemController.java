package br.edu.utfpr.server.controller;

import br.edu.utfpr.server.dto.ItemDTO;
import br.edu.utfpr.server.model.Item;
import br.edu.utfpr.server.service.ItemService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;

@RestController
@RequestMapping("/itens")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping
    public ResponseEntity<Page<ItemDTO>> getAllItens(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "nome,asc") String sort,
            @RequestParam(defaultValue = "") String search) {
        
        String[] sortParams = sort.split(",");
        String sortBy = sortParams[0];
        Sort.Direction direction = sortParams.length > 1 && "desc".equalsIgnoreCase(sortParams[1]) 
            ? Sort.Direction.DESC : Sort.Direction.ASC;
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        
        Page<Item> itens;
        if (search != null && !search.trim().isEmpty()) {
            itens = itemService.search(search.trim(), pageable);
        } else {
            itens = itemService.findAll(pageable);
        }
        
        Page<ItemDTO> itemDTOs = itens.map(ItemDTO::fromEntity);
        
        return ResponseEntity.ok(itemDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemDTO> getItemById(@PathVariable Long id) {
        return itemService.findById(id)
                .map(item -> ResponseEntity.ok(ItemDTO.fromEntity(item)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ItemDTO> createItem(@Valid @RequestBody ItemDTO itemDTO) {
        Item item = itemDTO.toEntity();
        Item savedItem = itemService.save(item);
        return new ResponseEntity<>(ItemDTO.fromEntity(savedItem), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ItemDTO> updateItem(
            @PathVariable Long id,
            @RequestParam("nome") String nome,
            @RequestParam("valor") BigDecimal valor,
            @RequestParam(value = "imagem", required = false) MultipartFile imagem) {
        
        Optional<Item> optionalItem = itemService.findById(id);
        
        if (optionalItem.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        try {
            Item existingItem = optionalItem.get();
            existingItem.setNome(nome);
            existingItem.setValor(valor);
            
            Item updatedItem = itemService.saveWithImage(existingItem, imagem);
            return ResponseEntity.ok(ItemDTO.fromEntity(updatedItem));
        } catch (IOException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        if (itemService.existsById(id)) {
            itemService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/imagem")
    public ResponseEntity<byte[]> getItemImage(@PathVariable Long id) {
        return itemService.findById(id)
                .filter(item -> item.getImagem() != null)
                .map(item -> ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(item.getTipoImagem()))
                        .body(item.getImagem()))
                .orElse(ResponseEntity.notFound().build());
    }
}
