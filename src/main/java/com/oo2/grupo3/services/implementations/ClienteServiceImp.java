package com.oo2.grupo3.services.implementations;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.oo2.grupo3.models.dtos.requests.ClienteRequestDTO;
import com.oo2.grupo3.models.dtos.requests.ExampleRequestDTO;
import com.oo2.grupo3.models.dtos.responses.ClienteResponseDTO;
import com.oo2.grupo3.models.dtos.responses.ExampleResponseDTO;
import com.oo2.grupo3.models.entities.Cliente;
import com.oo2.grupo3.models.entities.Example;
import com.oo2.grupo3.repositories.IClienteRepository;
import com.oo2.grupo3.repositories.IExampleRepository;
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
	public ClienteResponseDTO findById(int id) {
		Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Cliente with id {0} not found",id)));
		return modelMapper.map(cliente, ClienteResponseDTO.class);
	}
	
	@Override
	public ClienteResponseDTO findByName(String name) {
		Cliente cliente = clienteRepository.findById(name)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Cliente con ese nombre no se encontro",name)));
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
		Optional<Cliente> optionalCliente = clienteRepository.findById(id);
        if (optionalCliente.isPresent()) {
            clienteRepository.deleteById(id);
            return true;
        }
        return false;
	}

	
	

}
