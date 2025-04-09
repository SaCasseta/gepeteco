package com.example.hospital.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class Paciente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String cpf;
    private String telefone;

    @ManyToOne
    @JoinColumn(name = "medico_id")
    private Medico medico; // Relacionamento com médico

    @OneToMany(mappedBy = "paciente")
    private List<Consulta> consultas; // RQ4: Histórico do paciente
}