package br.edu.utfpr.server.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cliente")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Nome não pode ser nulo")
    @NotBlank(message = "Nome não pode estar vazio")
    @Size(max = 255, message = "Nome deve ter no máximo 255 caracteres")
    @Column(nullable = false)
    private String nome;

    @NotNull(message = "CPF não pode ser nulo")
    @NotBlank(message = "CPF não pode estar vazio")
    @Size(min = 11, max = 11, message = "CPF deve ter exatamente 11 caracteres")
    @Pattern(regexp = "\\d{11}", message = "CPF deve conter apenas números")
    @Column(unique = true, nullable = false, length = 11)
    private String cpf;
}
