package com.oo2.grupo3.repositories;


import java.io.Serializable;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.oo2.grupo3.models.entities.Dia;
import com.oo2.grupo3.models.entities.Hora;

public interface IHoraRepository extends JpaRepository<Hora, Serializable> {

    public abstract Optional<Hora> findByDia(Dia dia);
    
  
}
