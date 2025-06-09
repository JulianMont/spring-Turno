package com.oo2.grupo3.services.interfaces;

import java.util.List;
import java.util.Optional;

import com.oo2.grupo3.models.entities.Servicio;
import com.oo2.grupo3.models.entities.Ubicacion;

public interface IServicioService {
	public List <Servicio> getAll();
	
	public boolean remove (int id);
	
	public Optional<Servicio> findById(int id);
	
	public Servicio save(Servicio cliente);
	
	public Optional <Servicio> findByUbicacion (Ubicacion ubicacion);

	Optional<Servicio> findByNombre(String nombre);

}
