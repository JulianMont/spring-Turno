package com.oo2.grupo3.services.interfaces;

import java.util.List;
import java.util.Optional;

import com.oo2.grupo3.models.entities.Servicio;
import com.oo2.grupo3.models.entities.Ubicacion;

public interface IServicioService {
	
	
	public void deleteById(Integer id);
	
	public Optional<Servicio> findById(int id);
	
	public Optional<Servicio> findByNombre (String name);
	
	public Servicio save(Servicio cliente);
	
	public Optional <Servicio> findByUbicacion (Ubicacion ubicacion);
	public List<Servicio> getAll();
	

	

}
