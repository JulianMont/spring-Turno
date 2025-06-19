package com.oo2.grupo3.services.implementations;

import java.text.MessageFormat;
import java.util.List;

import java.util.Optional;
import java.util.stream.Collectors;


import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.oo2.grupo3.models.dtos.requests.ClienteRequestDTO;
import com.oo2.grupo3.models.dtos.responses.ClienteResponseDTO;


import com.oo2.grupo3.models.dtos.responses.TurnoResponseDTO;
import com.oo2.grupo3.models.entities.Cliente;

import com.oo2.grupo3.models.entities.Turno;
import com.oo2.grupo3.repositories.IClienteRepository;
import com.oo2.grupo3.repositories.ITurnoRepository;


import com.oo2.grupo3.models.entities.Cliente;
import com.oo2.grupo3.repositories.IClienteRepository;

import com.oo2.grupo3.services.interfaces.IClienteService;


import jakarta.persistence.EntityNotFoundException;
@Service
public class ClienteServiceImp implements IClienteService{
	
	private IClienteRepository clienteRepository;
	private ITurnoRepository turnoRepository;
	private final ModelMapper modelMapper;
	 
	 public ClienteServiceImp(IClienteRepository clienteRepository, ModelMapper modelMapper) {
	        this.clienteRepository = clienteRepository;
	        this.modelMapper = modelMapper;}
	
	 @Override
	 public Page<ClienteResponseDTO> getAll(Pageable pageable) {
	     return clienteRepository.findAll(pageable)
	             .map(cliente -> modelMapper.map(cliente, ClienteResponseDTO.class));
	 }

	 public List<ClienteResponseDTO> getAllClientes() {
	     List<Cliente> clientes = clienteRepository.findAll();
	     return clientes.stream()
	                    .map(cliente -> modelMapper.map(cliente, ClienteResponseDTO.class))
	                    .collect(Collectors.toList());
	 }


	@Override
	public ClienteResponseDTO findById(int id) {
		Cliente cliente = clienteRepository.findByIdPersona(id)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Cliente with id {0} not found",id)));
		return modelMapper.map(cliente, ClienteResponseDTO.class);
	}
	
	
		
	@Override
	public ClienteResponseDTO findByNombre(String nombre) {
		Cliente cliente = clienteRepository.findByNombre(nombre)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Cliente con ese nombre no se encontro",nombre)));
		return modelMapper.map(cliente, ClienteResponseDTO.class);
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

	
	@Override
	public List<TurnoResponseDTO> obtenerTurnosDelCliente(int idCliente) {
	    Cliente cliente = clienteRepository.findById(idCliente)
	            .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado"));

	    return cliente.getTurnosSolicitados()
	                  .stream()
	                  .map(turno -> modelMapper.map(turno, TurnoResponseDTO.class))
	                  .collect(Collectors.toList());
	}
	
	
	public List<TurnoResponseDTO> obtenerTodosLosTurnos() {
	   
	    List<Turno> turnos = turnoRepository.findAll();
	    return turnos.stream()
	             .map(turno -> modelMapper.map(turno, TurnoResponseDTO.class))
	             .collect(Collectors.toList());
	}

	

	@Override
	public List<ClienteResponseDTO> ordenadosPorNombre() {
	    List<Cliente> clientes = clienteRepository.findAllByOrderByNombreAsc();
	    return clientes.stream()
	    		.map(cliente -> modelMapper.map(cliente, ClienteResponseDTO.class))
	             .collect(Collectors.toList());
	}


	public ClienteResponseDTO save(ClienteRequestDTO clienteRequestDTO ) {
		Cliente cliente = modelMapper.map(clienteRequestDTO, Cliente.class);
		Cliente saved = clienteRepository.save(cliente);
        return modelMapper.map(saved, ClienteResponseDTO.class);
	}


	
	@Override
	public Page<ClienteResponseDTO> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Cliente save(Cliente cliente) {
		// TODO Auto-generated method stub
		return null;
	}

  
	

}
