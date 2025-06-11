package com.oo2.grupo3.services.interfaces;

import com.oo2.grupo3.models.entities.Hora;

import java.time.LocalTime;
import java.util.List;

public interface IHoraService {
    List<Hora> getAll();
    Hora findById(Integer id);
    List<Hora> getHorasPorDia(Integer idDia);
	Hora findByHora(LocalTime hora); 
}
