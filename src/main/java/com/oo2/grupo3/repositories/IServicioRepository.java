package com.oo2.grupo3.repositories;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oo2.grupo3.models.entities.Servicio;
import com.oo2.grupo3.models.entities.Ubicacion;


@Repository
public interface IServicioRepository extends JpaRepository<Servicio, Integer> {
		

	public void deleteById(Integer id);
	
	 public Optional<Servicio> findById(int id);
	
	 public Optional<Servicio> findByNombre(String name);
	
	 @SuppressWarnings("unchecked")
	 public Servicio save(Servicio cliente);
	
	 public Optional <Servicio> findByUbicacion (Ubicacion ubicacion);	
}

