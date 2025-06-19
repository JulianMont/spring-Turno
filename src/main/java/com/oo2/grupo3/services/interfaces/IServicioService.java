package com.oo2.grupo3.services.interfaces;

import java.util.List;
import java.util.Optional;

import com.oo2.grupo3.models.entities.Servicio;
import com.oo2.grupo3.models.entities.Ubicacion;

public interface IServicioService {
  
    void deleteById(Integer id);

    Optional<Servicio> findById(Integer id);

    Optional<Servicio> findByNombre(String name);

    Servicio save(Servicio servicio);

    Optional<Servicio> findByUbicacion(Ubicacion ubicacion);

    List<Servicio> getAll();
}