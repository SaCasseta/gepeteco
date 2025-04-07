package com.example.hospital.service;

import com.example.hospital.entity.Medico;
import com.example.hospital.repository.MedicoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MedicoService {

    private final MedicoRepository medicoRepository;

    // RQ1: Adicionar consultório a um médico
    public Medico adicionarConsultorio(Long medicoId, String consultorio) {
        Medico medico = medicoRepository.findById(medicoId)
                .orElseThrow(() -> new EntityNotFoundException("Médico não encontrado"));

        if (!medico.getConsultorios().contains(consultorio)) {
            medico.getConsultorios().add(consultorio);
            return medicoRepository.save(medico);
        }
        return medico;
    }

    // Buscar médico por CRM
    public Medico buscarPorCrm(String crm) {
        return medicoRepository.findByCrm(crm)
                .orElseThrow(() -> new EntityNotFoundException("CRM não encontrado"));
    }
}