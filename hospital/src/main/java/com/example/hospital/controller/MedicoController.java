package com.example.hospital.controller;


import com.example.hospital.entity.Medico;
import com.example.hospital.repository.MedicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medicos")
public class MedicoController {

    @Autowired
    private MedicoRepository medicoRepository;

    @GetMapping
    public List<Medico> listar() {
        return medicoRepository.findAll();
    }

    @PostMapping
    public Medico criar(@RequestBody Medico medico) {
        return medicoRepository.save(medico);
    }

    @PutMapping("/{id}/consultorio")
    public ResponseEntity<Medico> atualizarConsultorio(@PathVariable Long id, @RequestBody String consultorio) {
        return medicoRepository.findById(id)
                .map(medico -> {
                    medico.setConsultorio(consultorio); // RQ1
                    return ResponseEntity.ok(medicoRepository.save(medico));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}