package com.example.hospital.controller;

import com.example.hospital.entity.Paciente;
import com.example.hospital.repository.PacienteRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pacientes")
@RequiredArgsConstructor
public class PacienteController {

    private final PacienteRepository pacienteRepository;

    // POST - Criar paciente
    @PostMapping
    public ResponseEntity<Paciente> criarPaciente(@RequestBody @Valid Paciente paciente) {
        Paciente saved = pacienteRepository.save(paciente);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // PUT - Atualizar paciente
    @PutMapping("/{id}")
    public ResponseEntity<Paciente> atualizarPaciente(
            @PathVariable Long id,
            @RequestBody @Valid Paciente pacienteAtualizado) {

        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Paciente não encontrado"));

        paciente.setNome(pacienteAtualizado.getNome());
        paciente.setCpf(pacienteAtualizado.getCpf());

        return ResponseEntity.ok(pacienteRepository.save(paciente));
    }

    // DELETE - Remover paciente
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPaciente(@PathVariable Long id) {
        if (!pacienteRepository.existsById(id)) {
            throw new EntityNotFoundException("Paciente não encontrado");
        }
        pacienteRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }



    // GET - Listar todos (adicione se necessário)
    @GetMapping
    public ResponseEntity<List<Paciente>> listar() {
        return ResponseEntity.ok(pacienteRepository.findAll());
    }
}
