package com.oo2.grupo3.services.interfaces;

import java.util.List;
import java.util.Optional;

import com.oo2.grupo3.models.entities.Ubicacion;


public interface IUbicacionService {
	
	public List <Ubicacion> getAll();
	
	public boolean remove (int id);
	
	public Optional<Ubicacion> findById(int id);
	
	public Optional<Ubicacion> findByCiudad(String name);
	
	public Ubicacion save(Ubicacion ubicacion);

}
