package com.oo2.grupo3.services.implementations;

import java.text.MessageFormat;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.oo2.grupo3.models.dtos.requests.ClienteRequestDTO;
import com.oo2.grupo3.models.dtos.responses.ClienteResponseDTO;

import com.oo2.grupo3.models.entities.Cliente;
import com.oo2.grupo3.repositories.IClienteRepository;
import com.oo2.grupo3.services.interfaces.IClienteService;


import jakarta.persistence.EntityNotFoundException;
@Service
public class ClienteServiceImp implements IClienteService{
	
	private IClienteRepository clienteRepository;
	private final ModelMapper modelMapper;
	 
	 public ClienteServiceImp(IClienteRepository clienteRepository, ModelMapper modelMapper) {
	        this.clienteRepository = clienteRepository;
	        this.modelMapper = modelMapper;}
	
	 @Override
	 public Page<ClienteResponseDTO> getAll(Pageable pageable) {
	     return clienteRepository.findAll(pageable)
	             .map(cliente -> modelMapper.map(cliente, ClienteResponseDTO.class));
	 }

  
		
	@Override
	public ClienteResponseDTO findByNombre(String nombre) {
		Cliente cliente = clienteRepository.findByNombre(nombre)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Cliente con ese nombre no se encontro",nombre)));
		return modelMapper.map(cliente, ClienteResponseDTO.class);
	}

	@Override
	public ClienteResponseDTO save(ClienteRequestDTO clienteRequestDTO ) {
		Cliente cliente = modelMapper.map(clienteRequestDTO, Cliente.class);
		Cliente saved = clienteRepository.save(cliente);
        return modelMapper.map(saved, ClienteResponseDTO.class);
	}
	
	@Override
	public boolean remove(int id) {
		ClienteResponseDTO optionalCliente = clienteRepository.findById(id);
        if (optionalCliente.isPresent()) {
            clienteRepository.deleteById(id);
            return true;
        }
        return false;
	}
	@Override
	public ClienteResponseDTO findById(int id) {
		return clienteRepository.findById(id);
	}



	@Override
	public Cliente save(Cliente cliente) {
		 return clienteRepository.save(cliente);
	}

	@Override
	public ClienteResponseDTO findByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<ClienteResponseDTO> getAll() {
		// TODO Auto-generated method stub
		return null;
	}
  
	@Override
	public List<Cliente> getAllClientes() {
	    return clienteRepository.findAll();
	}
	

}
