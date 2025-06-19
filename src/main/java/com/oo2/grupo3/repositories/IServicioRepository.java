package com.oo2.grupo3.repositories;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oo2.grupo3.models.entities.Servicio;
import com.oo2.grupo3.models.entities.Ubicacion;


@Repository
public interface IServicioRepository extends JpaRepository<Servicio, Integer> {
  

    Optional<Servicio> findByNombre(String name);

    Optional<Servicio> findByUbicacion(Ubicacion ubicacion);
}

