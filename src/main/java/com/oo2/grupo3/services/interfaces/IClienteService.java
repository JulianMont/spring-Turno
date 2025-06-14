package com.oo2.grupo3.services.interfaces;

import java.util.List;
import java.util.Optional;

import com.oo2.grupo3.models.dtos.requests.ClienteRequestDTO;
import com.oo2.grupo3.models.dtos.responses.ClienteResponseDTO;
import com.oo2.grupo3.models.dtos.responses.TurnoResponseDTO;
import com.oo2.grupo3.models.entities.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface IClienteService {
	
	
	 Page<ClienteResponseDTO> getAll();
	
	public boolean remove (int id);
	
	ClienteResponseDTO findById(int id);
	

	ClienteResponseDTO findByNombre(String nombre);
	
	 ClienteResponseDTO save(ClienteRequestDTO clienteRequestDTO );
	 
	 List<TurnoResponseDTO> obtenerTurnosDelCliente(int idCliente);
	 
	 List<TurnoResponseDTO> obtenerTodosLosTurnos();

	List<ClienteResponseDTO> getAllClientes();


	
	
	// ClienteResponseDTO save(ClienteRequestDTO clienteRequestDTO );

	Cliente save(Cliente cliente);

	Page<ClienteResponseDTO> getAll(Pageable pageable);
	
	//public List<Cliente> getAllClientes();

	}

	

