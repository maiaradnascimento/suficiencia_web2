package br.edu.utfpr.server.dto;

import br.edu.utfpr.server.model.Item;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Base64;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemDTO {

    private Long id;

    @NotNull(message = "Nome não pode ser nulo")
    @NotBlank(message = "Nome não pode estar vazio")
    @Size(max = 255, message = "Nome deve ter no máximo 255 caracteres")
    private String nome;

    @NotNull(message = "Valor não pode ser nulo")
    @DecimalMin(value = "0.01", message = "Valor deve ser maior que zero")
    @Digits(integer = 8, fraction = 2, message = "Valor deve ter no máximo 8 dígitos inteiros e 2 decimais")
    private BigDecimal valor;

    private String imagemBase64;
    
    private String tipoImagem;

    public ItemDTO(Item item) {
        this.id = item.getId();
        this.nome = item.getNome();
        this.valor = item.getValor();
        this.tipoImagem = item.getTipoImagem();
        
        if (item.getImagem() != null) {
            this.imagemBase64 = Base64.getEncoder().encodeToString(item.getImagem());
        }
    }

    public Item toEntity() {
        Item item = new Item();
        item.setId(this.id);
        item.setNome(this.nome);
        item.setValor(this.valor);
        item.setTipoImagem(this.tipoImagem);
        
        if (this.imagemBase64 != null && !this.imagemBase64.isEmpty()) {
            try {
                item.setImagem(Base64.getDecoder().decode(this.imagemBase64));
            } catch (IllegalArgumentException e) {
                // Log erro se necessário
            }
        }
        
        return item;
    }
    
    public static ItemDTO fromEntity(Item item) {
        return new ItemDTO(item);
    }
}
