package com.oo2.grupo3.services.implementations;

import java.util.List;
import java.util.Optional;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.oo2.grupo3.models.entities.Notificacion;
import com.oo2.grupo3.models.entities.Persona;
import com.oo2.grupo3.repositories.INotificacionRepository;
import com.oo2.grupo3.services.interfaces.INotificacionService;

@Service
public class NotificacionServiceImp implements INotificacionService {
	private final INotificacionRepository notificacionRepository;
	private final JavaMailSender mailSender;
	
	public NotificacionServiceImp(INotificacionRepository notificacionRepository, JavaMailSender mailSender) {
        this.notificacionRepository = notificacionRepository;
        this.mailSender = mailSender;
    }
	
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
	    try {
	        enviarCorreo(notificacion);
	    } catch (Exception e) {
	        System.err.println("Error enviando correo: " + e.getMessage());
	        e.printStackTrace();
	    }
	    return notificacionRepository.save(notificacion);
	}

	
	private void enviarCorreo(Notificacion notificacion) {
	    Persona persona = notificacion.getPersona();

	    if (persona == null || persona.getUser() == null || persona.getUser().getEmail() == null) {
	        throw new IllegalStateException("La persona no tiene usuario o email asociado.");
	    }
	    System.out.println("Enviando email a: " + persona.getUser().getEmail());
	    SimpleMailMessage mensaje = new SimpleMailMessage();
	    mensaje.setTo(persona.getUser().getEmail());
	    mensaje.setSubject("Notificación: " + notificacion.getTipo());
	    mensaje.setText(notificacion.getMensaje());

	    mailSender.send(mensaje);
	}

}
