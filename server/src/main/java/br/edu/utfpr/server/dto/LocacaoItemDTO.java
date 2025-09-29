package br.edu.utfpr.server.dto;

import br.edu.utfpr.server.model.LocacaoItem;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocacaoItemDTO {

    private Long id;

    @NotNull(message = "Locação não pode ser nula")
    private Long locacaoId;

    @NotNull(message = "Item não pode ser nulo")
    private Long itemId;

    private ItemDTO item;

    @NotNull(message = "Quantidade não pode ser nula")
    @Min(value = 1, message = "Quantidade deve ser maior que zero")
    private Integer quantidade;

    @NotNull(message = "Valor não pode ser nulo")
    @DecimalMin(value = "0.01", message = "Valor deve ser maior que zero")
    @Digits(integer = 8, fraction = 2, message = "Valor deve ter no máximo 8 dígitos inteiros e 2 decimais")
    private BigDecimal valor;

    public LocacaoItemDTO(LocacaoItem locacaoItem) {
        this.id = locacaoItem.getId();
        this.locacaoId = locacaoItem.getLocacao().getId();
        this.itemId = locacaoItem.getItem().getId();
        this.item = new ItemDTO(locacaoItem.getItem());
        this.quantidade = locacaoItem.getQuantidade();
        this.valor = locacaoItem.getValor();
    }
}
