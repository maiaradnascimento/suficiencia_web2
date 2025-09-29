package br.edu.utfpr.server.controller;

import br.edu.utfpr.server.dto.LocacaoDTO;
import br.edu.utfpr.server.model.Locacao;
import br.edu.utfpr.server.service.LocacaoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/locacoes")
public class LocacaoController {

    private final LocacaoService locacaoService;

    public LocacaoController(LocacaoService locacaoService) {
        this.locacaoService = locacaoService;
    }

    @GetMapping
    public ResponseEntity<Page<LocacaoDTO>> getAllLocacoes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "data,desc") String sort,
            @RequestParam(defaultValue = "") String search) {
        
        String[] sortParams = sort.split(",");
        String sortBy = sortParams[0];
        Sort.Direction direction = sortParams.length > 1 && "desc".equalsIgnoreCase(sortParams[1]) 
            ? Sort.Direction.DESC : Sort.Direction.ASC;
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        
        Page<Locacao> locacoes;
        if (search != null && !search.trim().isEmpty()) {
            locacoes = locacaoService.search(search.trim(), pageable);
        } else {
            locacoes = locacaoService.findAll(pageable);
        }
        
        Page<LocacaoDTO> locacaoDTOs = locacoes.map(LocacaoDTO::fromEntity);
        
        return ResponseEntity.ok(locacaoDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LocacaoDTO> getLocacaoById(@PathVariable Long id) {
        return locacaoService.findById(id)
                .map(locacao -> ResponseEntity.ok(LocacaoDTO.fromEntity(locacao)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<LocacaoDTO> createLocacao(@Valid @RequestBody LocacaoDTO locacaoDTO) {
        try {
            Locacao locacao = locacaoService.createLocacao(locacaoDTO);
            return new ResponseEntity<>(LocacaoDTO.fromEntity(locacao), HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
