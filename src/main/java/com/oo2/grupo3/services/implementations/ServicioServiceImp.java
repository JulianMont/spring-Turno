package com.oo2.grupo3.services.implementations;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oo2.grupo3.models.entities.Servicio;
import com.oo2.grupo3.models.entities.Ubicacion;
import com.oo2.grupo3.repositories.IServicioRepository;
import com.oo2.grupo3.services.interfaces.IServicioService;

@Service
public class ServicioServiceImp implements IServicioService{
	
	private final IServicioRepository servicioRepository;

    public ServicioServiceImp(IServicioRepository servicioRepository) {
        this.servicioRepository = servicioRepository;
    }

	@Override
	public List<Servicio> getAll() {
		return servicioRepository.findAll();
	}

	@Override
	public boolean remove(int id) {
		Optional<Servicio> optionalServicio = servicioRepository.findById(id);
        if (optionalServicio.isPresent()) {
        	servicioRepository.deleteById(id);
            return true;
        }
       
		return false;
	}

	@Override
	public Optional<Servicio> findById(int id) {
		return servicioRepository.findById(id);
	}

	@Override
	public Optional<Servicio> findByNombre(String nombre) {
	
		return servicioRepository.findByNombre(nombre);
	}

	@Override
	public Servicio save(Servicio servicio) {
		
		return servicioRepository.save(servicio);
	}

	@Override
	public Optional<Servicio> findByUbicacion(Ubicacion ubicacion) {
		
		return servicioRepository.findByUbicacion(ubicacion);
	}

}
