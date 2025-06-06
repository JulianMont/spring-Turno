package com.oo2.grupo3.services.interfaces;

import java.util.List;
import java.util.Optional;

import com.oo2.grupo3.models.entities.Servicio;

public interface IServicioService {
	public List <Servicio> getAll();
	
	public boolean remove (int id);
	
	public Optional<Servicio> findById(int id);
	
	public Optional<Servicio> findByName(String name);
	
	public Servicio save(Servicio cliente);
	
	public Optional <Servicio> findByUbicacion (String ubicacion);

}
