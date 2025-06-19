package com.oo2.grupo3.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oo2.grupo3.models.entities.HorarioLaboral;

@Repository
public interface IHorarioLaboralRepository extends JpaRepository<HorarioLaboral, Integer> {
    
    List<HorarioLaboral> findByEmpleado_IdPersona(Integer empleadoId);
    
}