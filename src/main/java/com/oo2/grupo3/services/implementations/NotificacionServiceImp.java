package com.oo2.grupo3.services.implementations;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.oo2.grupo3.models.entities.Notificacion;
import com.oo2.grupo3.models.entities.Persona;
import com.oo2.grupo3.repositories.INotificacionRepository;
import com.oo2.grupo3.services.interfaces.INotificacionService;

@Service
public class NotificacionServiceImp implements INotificacionService {
	private INotificacionRepository notificacionRepository;

	@Override
	public List<Notificacion> getAll() {
		
		return notificacionRepository.findAll();
	}

	@Override
	public boolean remove(int id) {
		Optional<Notificacion> optionalNotificacion = notificacionRepository.findById(id);
        if (optionalNotificacion.isPresent()) {
        	notificacionRepository.deleteById(id);
            return true;
        }
		return false;
	}

	@Override
	public Optional<Notificacion> findById(int id) {
		return notificacionRepository.findById(id);
	}

	@Override
	public Optional<Notificacion> findByTipo(String ciudad) {
		
		return notificacionRepository.findByTipo(ciudad);
	}

	@Override
	public Optional<Notificacion> findByPersona(Persona persona) {
		
		return notificacionRepository.findByPersona(persona);
	}

	@Override
	public Notificacion save(Notificacion notificacion) {
		
		return notificacionRepository.save(notificacion);
	}
	

}
