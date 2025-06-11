package com.oo2.grupo3.repositories;


import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oo2.grupo3.models.entities.Dia;
import com.oo2.grupo3.models.entities.Hora;

@Repository

public interface IHoraRepository extends JpaRepository<Hora, Integer> {
    List<Hora> findByDiaId(Integer diaId);

    
    Optional<Hora> findByHora(LocalTime hora);
    List<Hora> findAllByHora(LocalTime hora);
    Optional<Hora> findByHoraAndDia(LocalTime hora, Dia dia);
    
}