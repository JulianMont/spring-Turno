package com.oo2.grupo3.services.implementations;

import java.util.List;
import java.util.Optional;


import com.oo2.grupo3.models.entities.Servicio;
import com.oo2.grupo3.repositories.IServiceRepository;
import com.oo2.grupo3.services.interfaces.IServicioService;

public class ServicioServiceImp implements IServicioService{
	
	private IServiceRepository serviceRepository;

	@Override
	public List<Servicio> getAll() {
		return serviceRepository.findAll();
	}

	@Override
	public boolean remove(int id) {
		Optional<Servicio> optionalServicio = serviceRepository.findById(id);
        if (optionalServicio.isPresent()) {
        	serviceRepository.deleteById(id);
            return true;
        }
       
		return false;
	}

	@Override
	public Optional<Servicio> findById(int id) {
		return serviceRepository.findById(id);
	}

	@Override
	public Optional<Servicio> findByName(String name) {
	
		return serviceRepository.findById(name);
	}

	@Override
	public Servicio save(Servicio servicio) {
		
		return serviceRepository.save(servicio);
	}

	@Override
	public Optional<Servicio> findByUbicacion(String ubicacion) {
		
		return serviceRepository.findByUbicacion(ubicacion);
	}

}
