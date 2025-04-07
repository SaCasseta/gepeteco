package com.example.hospital.controller;

import com.example.hospital.entity.Consulta;
import com.example.hospital.repository.ConsultaRepository;
import com.example.hospital.repository.MedicoRepository;
import com.example.hospital.repository.PacienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/consultas")
@RequiredArgsConstructor
public class ConsultaController {

    private final ConsultaRepository consultaRepository;
    private final MedicoRepository medicoRepository;
    private final PacienteRepository pacienteRepository;

    // RQ2: Agendar nova consulta
    @PostMapping
    public ResponseEntity<Consulta> agendar(@RequestBody Consulta consulta) {
        // Validações básicas
        if (!medicoRepository.existsById(consulta.getMedico().getId())) {
            throw new IllegalArgumentException("Médico não encontrado");
        }
        if (!pacienteRepository.existsById(consulta.getPaciente().getId())) {
            throw new IllegalArgumentException("Paciente não encontrado");
        }
        return ResponseEntity.ok(consultaRepository.save(consulta));
    }

    // RQ5: Agendar retorno
    @PostMapping("/{id}/retorno")
    public ResponseEntity<Consulta> agendarRetorno(
            @PathVariable Long id,
            @RequestParam LocalDateTime novaData) {

        Consulta original = consultaRepository.findById(id).orElseThrow();
        Consulta retorno = Consulta.builder()
                .medico(original.getMedico())
                .paciente(original.getPaciente())
                .consultorio(original.getConsultorio())
                .dataHora(novaData)
                .valor(original.getValor())
                .ehRetorno(true)
                .build();

        return ResponseEntity.ok(consultaRepository.save(retorno));
    }

    // RQ6: Gerar relatório financeiro
    @GetMapping("/relatorio")
    public ResponseEntity<Map<String, Object>> relatorio(
            @RequestParam Long medicoId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim) {

        List<Consulta> consultas = consultaRepository.findByMedicoIdAndDataHoraBetween(medicoId, inicio, fim);
        BigDecimal total = consultas.stream()
                .map(Consulta::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return ResponseEntity.ok(Map.of(
                "totalConsultas", consultas.size(),
                "valorTotal", total,
                "periodo", inicio + " a " + fim
        ));
    }
}
