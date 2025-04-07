package com.example.hospital.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(
        name = "medicos",
        uniqueConstraints = @UniqueConstraint(name = "uk_medico_crm", columnNames = "crm")
)
public class Medico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nome;

    @NotBlank(message = "CRM é obrigatório")
    @Pattern(regexp = "[A-Z]{2}\\d{4,6}", message = "CRM deve seguir o padrão XX9999")
    @Column(nullable = false, length = 10)
    private String crm;

    @NotBlank(message = "Especialidade é obrigatória")
    @Column(nullable = false, length = 50)
    private String especialidade;

    // RQ1: Gestão de consultórios
    @ElementCollection
    @CollectionTable(
            name = "medico_consultorios",
            joinColumns = @JoinColumn(name = "medico_id"),
            foreignKey = @ForeignKey(name = "fk_consultorios_medico")
    )
    @Column(name = "consultorio", nullable = false, length = 50)
    @Builder.Default
    @NotEmpty(message = "Pelo menos um consultório deve ser informado")
    @Size(min = 1, max = 5, message = "Máximo de 5 consultórios por médico")
    private List<@NotBlank(message = "Nome do consultório não pode ser vazio") String> consultorios = new ArrayList<>();

    // Método auxiliar para RQ1
    public void adicionarConsultorio(String consultorio) {
        if (consultorios.size() >= 5) {
            throw new IllegalStateException("Médico já atingiu o limite de consultórios");
        }
        consultorios.add(consultorio);
    }
}