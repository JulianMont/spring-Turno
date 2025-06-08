package com.oo2.grupo3.repositories;

import java.io.Serializable;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.oo2.grupo3.models.entities.Notificacion;
import com.oo2.grupo3.models.entities.Persona;



public interface INotificacionRepository extends JpaRepository<Notificacion, Serializable> {
	
	public abstract Optional <Notificacion> findById (int id);
	
	public abstract Optional <Notificacion> findByTipo (String ciudad);
	
	public abstract Optional <Notificacion> findByPersona (Persona persona);
	

}
