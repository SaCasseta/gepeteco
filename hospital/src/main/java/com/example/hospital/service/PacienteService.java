package com.example.hospital.service;

import com.example.hospital.entity.Consulta;
import com.example.hospital.entity.Paciente;
import com.example.hospital.repository.ConsultaRepository;
import com.example.hospital.repository.PacienteRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PacienteService {
    private final PacienteRepository pacienteRepository;
    private final ConsultaRepository consultaRepository;

    // RQ3: Listar pacientes de um médico (com tratamento completo)
    public List<Paciente> listarPorMedico(Long medicoId) {
        // Verifica se o médico existe primeiro
        if (!consultaRepository.existsByMedicoId(medicoId)) {
            return Collections.emptyList();
        }

        List<Consulta> consultas = consultaRepository.findByMedicoId(medicoId);
        List<Paciente> pacientes = new ArrayList<>();

        // Filtro manual seguro
        for (Consulta consulta : consultas) {
            if (consulta.getPaciente() != null && !pacientes.contains(consulta.getPaciente())) {
                pacientes.add(consulta.getPaciente());
            }
        }

        return pacientes;
    }

    // RQ4: Buscar histórico com validação
    public List<Consulta> buscarHistorico(Long pacienteId) {
        if (!pacienteRepository.existsById(pacienteId)) {
            throw new EntityNotFoundException("Paciente não encontrado");
        }
        return consultaRepository.findByPacienteIdOrderByDataHoraDesc(pacienteId);
    }
}