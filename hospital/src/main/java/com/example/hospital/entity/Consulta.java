package com.example.hospital.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Consulta {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // RQ2: Agendamento com médico
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medico_id")
    private Medico medico;

    // RQ3: Controle por paciente
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paciente_id")
    private Paciente paciente;

    // RQ1: Consultório utilizado
    private String consultorio;

    private LocalDateTime dataHora;
    private BigDecimal valor;

    // RQ5: Flag para retornos
    private boolean ehRetorno;

    // RQ6: Método auxiliar para relatório
    public boolean isDentroPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        return dataHora.isAfter(inicio) && dataHora.isBefore(fim);
    }
}