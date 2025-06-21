package com.oo2.grupo3.controllers;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.oo2.grupo3.helpers.ViewRouteHelper;
import com.oo2.grupo3.models.dtos.requests.ClienteRequestDTO;
import com.oo2.grupo3.models.dtos.responses.ClienteResponseDTO;
import com.oo2.grupo3.models.dtos.responses.EmpleadoResponseDTO;
import com.oo2.grupo3.models.dtos.responses.TurnoResponseDTO;
import com.oo2.grupo3.models.entities.Cliente;
import com.oo2.grupo3.services.interfaces.IClienteService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/cliente")
public class ClienteController {

	private final IClienteService clienteService;
    private final ModelMapper modelMapper;
	
	public ClienteController(IClienteService clienteService, ModelMapper modelMapper) {
		this.clienteService = clienteService;
        this.modelMapper = modelMapper;
	}
	
	
    @GetMapping ("/list")
    public String listarClientes(Model model) {
        List<ClienteResponseDTO> clientes = clienteService.ordenadosPorNombre(); 
        model.addAttribute("clientes", clientes);
        return "cliente/list";
    }

    @GetMapping("/guardar")
    public String mostrarFormularioNuevoCliente(Model model) {
        model.addAttribute("cliente", new ClienteRequestDTO());
        return "cliente/form";
    }
    //TODO: No edita porque no tenes update,post o save
    @GetMapping("/editar/{id}")
    public String editarCliente(@PathVariable int id, Model model) {
        ClienteResponseDTO cliente = clienteService.findById(id);
        model.addAttribute("cliente", cliente);
        return "cliente/form";
    }
    
    
    @PostMapping("/guardar")
    public String guardarCliente(@Valid @ModelAttribute("cliente") ClienteRequestDTO clienteDTO,
                                 BindingResult result,
                                 Model model) {
        if (result.hasErrors()) {
            return "cliente/form";
        }

        try {
            clienteService.save(clienteDTO);
        }  catch (IllegalArgumentException e) {
            model.addAttribute("errorDni", e.getMessage()); // por ejemplo: "Ya existe un cliente con este DNI"
            return "cliente/form";
            
            }catch (DataIntegrityViolationException e) {
            // Si ya existe un cliente con ese DNI (clave única)
            model.addAttribute("errorDni", "Ya existe un cliente con ese DNI.");
            return "cliente/form";
        }

        return "redirect:/cliente/list";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarCliente(@PathVariable int id) {
        clienteService.remove(id);
        return "redirect:/cliente/list";
    }
}

//// --- SOLO ADMIN ---

/*@PreAuthorize("hasRole('ADMIN')")
@GetMapping("/form")
public String mostrarFormularioCrear(Model model) {
  model.addAttribute("clienteRequestDTO", new ClienteRequestDTO());
  return ViewRouteHelper.CLIENTES_FORM;
}*/


