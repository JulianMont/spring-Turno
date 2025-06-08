package com.oo2.grupo3.services.implementations;

import java.util.List;
import java.util.Optional;

import com.oo2.grupo3.models.entities.Cliente;
import com.oo2.grupo3.models.entities.Contacto;
import com.oo2.grupo3.repositories.IContactoRepository;
import com.oo2.grupo3.services.interfaces.IContactoService;

public class ContactoServiceImp implements IContactoService{
	private IContactoRepository contactoRepository;

	@Override
	public List<Contacto> getAll() {
		
		return contactoRepository.findAll();
	}

	@Override
	public boolean remove(int id) {
		Optional<Contacto> optionalContacto = contactoRepository.findById(id);
        if (optionalContacto.isPresent()) {
        	contactoRepository.deleteById(id);
            return true;}
		return false;
	}

	@Override
	public Optional<Contacto> findById(int id) {
		
		return contactoRepository.findById(id);
	}

	@Override
	public Optional<Contacto> findByEmail(String email) {
		
		return contactoRepository.findByEmail(email);
	}

	@Override
	public Optional<Contacto> findByTelefono(String telefono) {
		
		return contactoRepository.findByTelefono(telefono);
	}

	@Override
	public Contacto save(Contacto contacto) {
		
		return contactoRepository.save(contacto);
	}
	
	

}
