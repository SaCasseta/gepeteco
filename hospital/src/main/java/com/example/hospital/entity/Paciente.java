package com.example.hospital.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(
        name = "pacientes",
        uniqueConstraints = @UniqueConstraint(name = "uk_paciente_cpf", columnNames = "cpf")
)
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nome;

    @NotBlank(message = "CPF é obrigatório")
    @CPF(message = "CPF inválido")
    @Column(nullable = false, length = 14, unique = true)
    private String cpf;

    // RQ4: Histórico de consultas
    @OneToMany(
            mappedBy = "paciente",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @Builder.Default
    @OrderBy("dataHora DESC")  // Ordena por data decrescente
    private List<Consulta> historico = new ArrayList<>();

    // Método auxiliar para RQ4 (adicionar consulta ao histórico)
    public void adicionarConsulta(Consulta consulta) {
        consulta.setPaciente(this);
        historico.add(consulta);
    }

    // Método auxiliar para RQ3 (buscar consultas recentes)
    public List<Consulta> getUltimasConsultas(int limite) {
        return historico.stream()
                .limit(limite)
                .toList();
    }
}