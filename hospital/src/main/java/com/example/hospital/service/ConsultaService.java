package com.example.hospital.service;

import com.example.hospital.entity.Consulta;
import com.example.hospital.repository.ConsultaRepository;
import com.example.hospital.repository.MedicoRepository;
import com.example.hospital.repository.PacienteRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConsultaService {
    private final ConsultaRepository consultaRepository;
    private final MedicoRepository medicoRepository;
    private final PacienteRepository pacienteRepository;

    // RQ2: Agendar consulta com validações
    public Consulta agendar(Consulta consulta) {
        // Valida existência do médico e paciente
        if (!medicoRepository.existsById(consulta.getMedico().getId())) {
            throw new EntityNotFoundException("Médico não encontrado");
        }
        if (!pacienteRepository.existsById(consulta.getPaciente().getId())) {
            throw new EntityNotFoundException("Paciente não encontrado");
        }

        // Valida conflito de horário (mesmo médico)
        boolean conflito = consultaRepository.existsByMedicoIdAndDataHoraBetween(
                consulta.getMedico().getId(),
                consulta.getDataHora().minusMinutes(29),
                consulta.getDataHora().plusMinutes(29)
        );
        if (conflito) {
            throw new IllegalStateException("Médico já possui consulta neste horário");
        }

        return consultaRepository.save(consulta);
    }

    // RQ5: Agendar retorno
    public Consulta agendarRetorno(Long consultaId, LocalDateTime novaData) {
        Consulta original = consultaRepository.findById(consultaId)
                .orElseThrow(() -> new EntityNotFoundException("Consulta original não encontrada"));

        Consulta retorno = Consulta.builder()
                .medico(original.getMedico())
                .paciente(original.getPaciente())
                .consultorio(original.getConsultorio())
                .dataHora(novaData)
                .valor(original.getValor())
                .ehRetorno(true)
                .build();

        return consultaRepository.save(retorno);
    }

    // RQ6: Relatório financeiro
    public BigDecimal calcularTotalPeriodo(Long medicoId, LocalDateTime inicio, LocalDateTime fim) {
        List<Consulta> consultas = consultaRepository.findByMedicoIdAndDataHoraBetween(medicoId, inicio, fim);
        return consultas.stream()
                .map(Consulta::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}

