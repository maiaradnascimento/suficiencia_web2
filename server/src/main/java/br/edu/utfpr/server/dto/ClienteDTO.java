package br.edu.utfpr.server.dto;

import br.edu.utfpr.server.model.Cliente;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteDTO {

    private Long id;

    @NotNull(message = "Nome não pode ser nulo")
    @NotBlank(message = "Nome não pode estar vazio")
    @Size(max = 255, message = "Nome deve ter no máximo 255 caracteres")
    private String nome;

    @NotNull(message = "CPF não pode ser nulo")
    @NotBlank(message = "CPF não pode estar vazio")
    @Size(min = 11, max = 11, message = "CPF deve ter exatamente 11 caracteres")
    @Pattern(regexp = "\\d{11}", message = "CPF deve conter apenas números")
    private String cpf;

    public ClienteDTO(Cliente cliente) {
        this.id = cliente.getId();
        this.nome = cliente.getNome();
        this.cpf = cliente.getCpf();
    }

    public Cliente toEntity() {
        Cliente cliente = new Cliente();
        cliente.setId(this.id);
        cliente.setNome(this.nome);
        cliente.setCpf(this.cpf);
        return cliente;
    }
}
