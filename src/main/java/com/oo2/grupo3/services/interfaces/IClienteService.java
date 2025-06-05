package com.oo2.grupo3.services.interfaces;

import java.util.List;
import java.util.Optional;

import com.oo2.grupo3.models.entities.Cliente;

public interface IClienteService {
	
	public List <Cliente> getAll();
	
	public boolean remove (int id);
	
	public Optional<Cliente> findById(int id);
	
	public Optional<Cliente> findByName(String name);
	
	public Cliente save(Cliente cliente);

	}

	

