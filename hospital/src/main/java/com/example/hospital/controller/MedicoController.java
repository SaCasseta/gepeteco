package com.example.hospital.controller;

import com.example.hospital.entity.Medico;
import com.example.hospital.repository.MedicoRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/medicos")
@RequiredArgsConstructor
public class MedicoController {

    private final MedicoRepository medicoRepository;

    // POST - Criar médico
    @PostMapping
    public ResponseEntity<Medico> criarMedico(@RequestBody @Valid Medico medico) {
        Medico saved = medicoRepository.save(medico);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // PUT - Atualizar médico
    @PutMapping("/{id}")
    public ResponseEntity<Medico> atualizarMedico(
            @PathVariable Long id,
            @RequestBody @Valid Medico medicoAtualizado) {

        Medico medico = medicoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Médico não encontrado"));

        medico.setNome(medicoAtualizado.getNome());
        medico.setCrm(medicoAtualizado.getCrm());
        medico.setEspecialidade(medicoAtualizado.getEspecialidade());

        return ResponseEntity.ok(medicoRepository.save(medico));
    }

    // DELETE - Remover médico
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarMedico(@PathVariable Long id) {
        if (!medicoRepository.existsById(id)) {
            throw new EntityNotFoundException("Médico não encontrado");
        }
        medicoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // GET - Listar todos (já existente)
    @GetMapping
    public ResponseEntity<List<Medico>> listar() {
        return ResponseEntity.ok(medicoRepository.findAll());
    }
}