package br.edu.utfpr.server.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "locacao")
public class Locacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Número da nota não pode ser nulo")
    @NotBlank(message = "Número da nota não pode estar vazio")
    @Size(max = 50, message = "Número da nota deve ter no máximo 50 caracteres")
    @Column(unique = true, nullable = false, length = 50)
    private String numnota;

    @NotNull(message = "Data não pode ser nula")
    @Column(nullable = false)
    private LocalDate data;

    @NotNull(message = "Cliente não pode ser nulo")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Cliente cliente;

    @OneToMany(mappedBy = "locacao", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @ToString.Exclude
    private List<LocacaoItem> itens;
}
