package com.oo2.grupo3.repositories;


import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.oo2.grupo3.models.dtos.responses.ClienteResponseDTO;
import com.oo2.grupo3.models.entities.Cliente;

@Repository
public interface IClienteRepository extends JpaRepository<Cliente, Serializable> {
	
	public abstract Optional<Cliente> findByIdPersona(int id);
 
	
	public abstract Optional <Cliente> findByNombre (String name);
	List<Cliente> findAllByOrderByNombreAsc();



}