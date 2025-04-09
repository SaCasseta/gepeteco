package com.example.hospital.controller;

import com.example.hospital.entity.Consulta;
import com.example.hospital.repository.ConsultaRepository;
import com.example.hospital.repository.MedicoRepository;
import com.example.hospital.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/consultas")
public class ConsultaController {

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @GetMapping("/medico/{medicoId}")
    public List<Consulta> listarPorMedico(@PathVariable Long medicoId) {
        return consultaRepository.findByMedicoId(medicoId); // RQ2, RQ3
    }

    @GetMapping("/paciente/{pacienteId}")
    public List<Consulta> listarPorPaciente(@PathVariable Long pacienteId) {
        return consultaRepository.findByPacienteId(pacienteId); // RQ4
    }

    @PostMapping
    public ResponseEntity<Consulta> agendar(@RequestBody Consulta consulta) {
        return medicoRepository.findById(consulta.getMedico().getId())
                .map(medico -> {
                    pacienteRepository.findById(consulta.getPaciente().getId())
                            .map(paciente -> {
                                consulta.setMedico(medico);
                                consulta.setPaciente(paciente);
                                return ResponseEntity.ok(consultaRepository.save(consulta)); // RQ2, RQ5
                            })
                            .orElse(ResponseEntity.badRequest().build());
                    return ResponseEntity.ok(consulta);
                })
                .orElse(ResponseEntity.badRequest().build());
    }

    @GetMapping("/relatorio-financeiro/{medicoId}")
    public ResponseEntity<Double> relatorioFinanceiro(@PathVariable Long medicoId) {
        List<Consulta> consultas = consultaRepository.findByMedicoId(medicoId);
        double total = consultas.stream().mapToDouble(Consulta::getValor).sum();
        return ResponseEntity.ok(total); // RQ6
    }
}