package com.oo2.grupo3.repositories;

import java.io.Serializable;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.oo2.grupo3.models.entities.Servicio;

public interface IServiceRepository extends JpaRepository<Servicio, Serializable> {
	
	public abstract Optional <Servicio> findById (int id);
	
	public abstract Optional <Servicio> findByName (String name);
	
	public abstract Optional <Servicio> findByUbicacion (String ubicacion);

}
