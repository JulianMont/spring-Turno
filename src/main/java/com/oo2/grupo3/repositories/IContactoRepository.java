package com.oo2.grupo3.repositories;

import java.io.Serializable;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


import com.oo2.grupo3.models.entities.Contacto;

public interface IContactoRepository extends JpaRepository<Contacto, Serializable>{
	
	public abstract Optional <Contacto> findById (int id);
	
	public abstract Optional <Contacto> findByEmail (String email);
	
	public abstract Optional <Contacto> findByTelefono (String telefono);
	

	

}
