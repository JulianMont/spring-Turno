package com.oo2.grupo3.controllers;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
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
	
	
	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/list")
    public String listarClientes(
            @RequestParam(required = false) String nombre,
            Pageable pageable,
            Model model
    ) {
        Page<EmpleadoResponseDTO> cliente;

        
        return ViewRouteHelper.CLENTES_LIST;
        
    }
	
    
//    // --- SOLO ADMIN ---

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/form")
    public String mostrarFormularioCrear(Model model) {
        model.addAttribute("clienteRequestDTO", new ClienteRequestDTO());
        return ViewRouteHelper.CLIENTES_FORM;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/save")
    public String crearCliente(@Valid @ModelAttribute ClienteRequestDTO dto,
                                BindingResult result,
                                Model model) {
        if (result.hasErrors()) {
        	
        }

        return ViewRouteHelper.REDIRECT_CLIENTES_LIST;
    }
    
        
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/eliminar/{id}")
    public String eliminarCliente(@PathVariable Integer id) {
        clienteService.remove(id);
        return ViewRouteHelper.REDIRECT_CLIENTES_LIST;
    }





    @GetMapping ("/list")
    public String listarClientes(Model model) {
        List<ClienteResponseDTO> clientes = clienteService.getAllClientes(); 
        model.addAttribute("clientes", clientes);
        return "cliente/list";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevoCliente(Model model) {
        model.addAttribute("cliente", new ClienteRequestDTO());
        return "cliente/form";
    }

   
    @PostMapping("/guardar")
    public String guardarCliente(@Valid @ModelAttribute("cliente") ClienteRequestDTO clienteDTO, 
                                 BindingResult result,
                                 Model model) {
        if (result.hasErrors()) {
            return "cliente/form"; 
        }

        clienteService.save(clienteDTO);
        return "redirect:/cliente/list";}


    @GetMapping("/editar/{id}")
    public String editarCliente(@PathVariable int id, Model model) {
        ClienteResponseDTO cliente = clienteService.findById(id);
        model.addAttribute("cliente", cliente);
        return "cliente/form";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarCliente(@PathVariable int id) {
        clienteService.remove(id);
        return "redirect:/cliente";
    }
}



