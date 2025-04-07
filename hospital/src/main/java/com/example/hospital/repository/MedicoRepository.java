package com.example.hospital.repository;

import com.example.hospital.entity.Medico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MedicoRepository extends JpaRepository<Medico, Long> {

    // RQ1: Buscar médicos por consultório
    @Query("SELECT m FROM Medico m WHERE :consultorio MEMBER OF m.consultorios")
    List<Medico> findByConsultorio(@Param("consultorio") String consultorio);

    // Busca por CRM
    Optional<Medico> findByCrm(String crm);
}