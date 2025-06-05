package com.oo2.grupo3.services.interfaces;

import java.util.List;
import java.util.Optional;

import com.oo2.grupo3.models.entities.Notificacion;
import com.oo2.grupo3.models.entities.Persona;

public interface INotificacionService {
	
	public List<Notificacion> getAll();
	
	public boolean remove (int id);
	
	public  Optional <Notificacion> findById (int id);
	
	public  Optional <Notificacion> findByTipo (String ciudad);
	
	public  Optional <Notificacion> findByPersona (Persona persona);
	
	public Notificacion save (Notificacion notificacion);

}
