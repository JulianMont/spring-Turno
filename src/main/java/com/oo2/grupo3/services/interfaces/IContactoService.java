package com.oo2.grupo3.services.interfaces;

import java.util.List;
import java.util.Optional;

import com.oo2.grupo3.models.entities.Contacto;

public interface IContactoService {
	
	public List <Contacto> getAll();
	
	public boolean remove (int id);
	
	public Optional<Contacto> findById(int id);
	
	public Optional<Contacto> findByEmail(String email);
	
	public Optional <Contacto> findByTelefono (String telefono);
	
	public Contacto save(Contacto contacto);
	

}
