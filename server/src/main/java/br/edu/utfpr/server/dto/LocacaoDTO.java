package br.edu.utfpr.server.dto;

import br.edu.utfpr.server.model.Locacao;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocacaoDTO {

    private Long id;

    @NotNull(message = "Número da nota não pode ser nulo")
    @NotBlank(message = "Número da nota não pode estar vazio")
    @Size(max = 50, message = "Número da nota deve ter no máximo 50 caracteres")
    private String numnota;

    @NotNull(message = "Data não pode ser nula")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate data;

    @NotNull(message = "Cliente não pode ser nulo")
    private Long clienteId;

    private ClienteDTO cliente;

    private List<LocacaoItemDTO> itens;
    
    private List<LocacaoItemDTO> locacaoItems;

    public LocacaoDTO(Locacao locacao) {
        this.id = locacao.getId();
        this.numnota = locacao.getNumnota();
        this.data = locacao.getData();
        this.clienteId = locacao.getCliente().getId();
        this.cliente = new ClienteDTO(locacao.getCliente());
        
        if (locacao.getItens() != null) {
            this.itens = locacao.getItens().stream()
                    .map(LocacaoItemDTO::new)
                    .collect(Collectors.toList());
            this.locacaoItems = this.itens;
        }
    }
    
    public static LocacaoDTO fromEntity(Locacao locacao) {
        return new LocacaoDTO(locacao);
    }
    
    public Locacao toEntity() {
        Locacao locacao = new Locacao();
        locacao.setId(this.id);
        locacao.setNumnota(this.numnota);
        locacao.setData(this.data);
        return locacao;
    }
}
