package com.example.hospital.repository;

import com.example.hospital.entity.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    // RQ3/RQ4: Buscar pacientes de um médico com histórico
    @Query("SELECT DISTINCT p FROM Paciente p JOIN p.historico h WHERE h.medico.id = :medicoId")
    List<Paciente> findByMedicoId(@Param("medicoId") Long medicoId);

    // Busca por CPF
    Optional<Paciente> findByCpf(String cpf);
}