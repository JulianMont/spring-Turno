package com.oo2.grupo3.services.implementations;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.oo2.grupo3.models.entities.Ubicacion;
import com.oo2.grupo3.repositories.IUbicacionRepository;
import com.oo2.grupo3.services.interfaces.IUbicacionService;

@Service
public class UbicacionServiceImp implements IUbicacionService{
	private IUbicacionRepository ubicacionRepository;

	@Override
	public List<Ubicacion> getAll() {
		
		return ubicacionRepository.findAll();
	}

	@Override
	public boolean remove(int id) {
		Optional<Ubicacion> optionalUbicacion = ubicacionRepository.findById(id);
        if (optionalUbicacion.isPresent()) {
        	ubicacionRepository.deleteById(id);
            return true;
        }
        return false;
	}

	@Override
	public Optional<Ubicacion> findById(int id) {
		
		return ubicacionRepository.findById(id);
	}

	@Override
	public Optional<Ubicacion> findByCiudad(String name) {
		
		return ubicacionRepository.findByCiudad(name);
	}

	@Override
	public Ubicacion save(Ubicacion ubicacion) {
		
		return ubicacionRepository.save(ubicacion);
	}

}
