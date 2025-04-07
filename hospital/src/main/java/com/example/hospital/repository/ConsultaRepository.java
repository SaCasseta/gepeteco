package com.example.hospital.repository;

import com.example.hospital.entity.Consulta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {

    // RQ2: Consultas por médico e período
    List<Consulta> findByMedicoIdAndDataHoraBetween(
            Long medicoId,
            LocalDateTime inicio,
            LocalDateTime fim
    );

    // RQ5: Retornos de um paciente
    List<Consulta> findByPacienteIdAndEhRetornoTrue(Long pacienteId);

    // RQ6: Consultas com valor em um período (para relatório)
    @Query("SELECT SUM(c.valor) FROM Consulta c WHERE c.medico.id = :medicoId " +
            "AND c.dataHora BETWEEN :inicio AND :fim")
    BigDecimal calcularTotalPeriodo(
            @Param("medicoId") Long medicoId,
            @Param("inicio") LocalDateTime inicio,
            @Param("fim") LocalDateTime fim
    );

    boolean existsByMedicoIdAndDataHoraBetween(Long id, LocalDateTime localDateTime, LocalDateTime localDateTime1);

    List<Consulta> findByPacienteIdOrderByDataHoraDesc(Long pacienteId);

    List<Consulta> findByMedicoId(Long medicoId);

    boolean existsByMedicoId(Long medicoId);
}