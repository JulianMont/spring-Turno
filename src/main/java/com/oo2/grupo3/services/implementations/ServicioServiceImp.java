package com.oo2.grupo3.services.implementations;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.oo2.grupo3.models.entities.Servicio;
import com.oo2.grupo3.models.entities.Ubicacion;
import com.oo2.grupo3.repositories.IServicioRepository;
import com.oo2.grupo3.services.interfaces.IServicioService;
@Service
public class ServicioServiceImp implements IServicioService{
	
	private IServicioRepository serviceRepository;

	
	public void deleteById(Integer id) {
		Optional<Servicio> optionalServicio = serviceRepository.findById(id);
        if (optionalServicio.isPresent()) {
        	serviceRepository.deleteById(id);
          
       
		
	}}

	@Override
	public Optional<Servicio> findById(int id) {
		return serviceRepository.findById(id);
	}

	@Override
	public Optional<Servicio> findByNombre(String name) {
	
		return serviceRepository.findByNombre(name);
	}

	@Override
	public Servicio save(Servicio servicio) {
		
		return serviceRepository.save(servicio);
	}

	@Override
	public Optional<Servicio> findByUbicacion(Ubicacion ubicacion) {
		
		return serviceRepository.findByUbicacion(ubicacion);
	}

}
