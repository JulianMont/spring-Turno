package com.oo2.grupo3.repositories;


import java.io.Serializable;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.oo2.grupo3.models.entities.Cliente;


public interface IClienteRepository extends JpaRepository<Cliente, Serializable> {
	
	public abstract Optional <Cliente> findById (int id);
	
	public abstract Optional <Cliente> findByName (String name);

}