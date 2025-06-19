package com.oo2.grupo3.services.interfaces;

import com.oo2.grupo3.models.entities.Dia;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IDiaService {
    List<Dia> getAll();
    Dia findById(Integer id);
    
    Optional<Dia> findByFecha(LocalDate fecha);
}
