package com.oo2.grupo3.repositories;

import java.io.Serializable;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.oo2.grupo3.models.entities.Ubicacion;

public interface IUbicacionRepository extends JpaRepository<Ubicacion, Serializable> {
	
	public abstract Optional <Ubicacion> findById (int id);
	
	public abstract Optional <Ubicacion> findByCiudad (String ciudad);
	 

}
