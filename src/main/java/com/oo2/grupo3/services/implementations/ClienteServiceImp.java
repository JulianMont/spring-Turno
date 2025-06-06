package com.oo2.grupo3.services.implementations;

import java.util.List;
import java.util.Optional;

import com.oo2.grupo3.models.entities.Cliente;
import com.oo2.grupo3.repositories.IClienteRepository;
import com.oo2.grupo3.services.interfaces.IClienteService;

public class ClienteServiceImp implements IClienteService{
	
	private IClienteRepository clienteRepository;
	
	@Override
	public List<Cliente> getAll() {
		return clienteRepository.findAll();
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
	public Optional<Cliente> findById(int id) {
		return clienteRepository.findById(id);
	}

	@Override
	public Optional<Cliente> findByName(String name) {
		return clienteRepository.findByName(name);
	}

	@Override
	public Cliente save(Cliente cliente) {
		 return clienteRepository.save(cliente);
	}
	
	

}
