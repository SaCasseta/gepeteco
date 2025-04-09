package com.example.hospital.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class Medico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String especialidade;
    private String consultorio; // RQ1: Gerir consultório

    @OneToMany(mappedBy = "medico")
    private List<Paciente> pacientes; // RQ3: Controle de lista de pacientes

    @OneToMany(mappedBy = "medico")
    private List<Consulta> consultas; // RQ2, RQ5: Agendar consultas e retornos
}