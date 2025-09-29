package br.edu.utfpr.server.controller;

import br.edu.utfpr.server.dto.ClienteDTO;
import br.edu.utfpr.server.model.Cliente;
import br.edu.utfpr.server.repository.ClienteRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/clientes")
@CrossOrigin(maxAge = 3600)
public class ClienteController {

    private final ClienteRepository clienteRepository;

    public ClienteController(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @GetMapping
    public ResponseEntity<List<ClienteDTO>> getAllClientes() {
        List<Cliente> clientes = clienteRepository.findAllOrderByNome();
        List<ClienteDTO> clienteDTOs = clientes.stream()
                .map(ClienteDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(clienteDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteDTO> getClienteById(@PathVariable Long id) {
        return clienteRepository.findById(id)
                .map(cliente -> ResponseEntity.ok(new ClienteDTO(cliente)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ClienteDTO> createCliente(@RequestBody ClienteDTO clienteDTO) {
        if (clienteRepository.existsByCpf(clienteDTO.getCpf())) {
            return ResponseEntity.badRequest().build();
        }
        
        Cliente cliente = clienteDTO.toEntity();
        Cliente savedCliente = clienteRepository.save(cliente);
        return ResponseEntity.ok(new ClienteDTO(savedCliente));
    }
}
