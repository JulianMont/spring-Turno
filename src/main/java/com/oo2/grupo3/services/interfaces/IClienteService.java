package com.oo2.grupo3.services.interfaces;

import java.util.List;
import java.util.Optional;

import com.oo2.grupo3.models.dtos.requests.ClienteRequestDTO;
import com.oo2.grupo3.models.dtos.responses.ClienteResponseDTO;
import com.oo2.grupo3.models.entities.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface IClienteService {
	
	
	 Page<ClienteResponseDTO> getAll(Pageable pageable);
	
	public boolean remove (int id);
	
	ClienteResponseDTO findById(int id);
	
	ClienteResponseDTO findByName(String name);
	
	 ClienteResponseDTO save(ClienteRequestDTO clienteRequestDTO );
	}

	

