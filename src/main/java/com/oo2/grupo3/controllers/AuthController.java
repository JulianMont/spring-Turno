package com.oo2.grupo3.controllers;

import com.oo2.grupo3.helpers.ViewRouteHelper;
import com.oo2.grupo3.models.dtos.requests.ClienteRequestDTO;
import com.oo2.grupo3.services.interfaces.IClienteService;

import jakarta.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/auth")
public class AuthController {
 
	private final IClienteService clienteService;
    private final ModelMapper modelMapper;
	
	public AuthController(IClienteService clienteService, ModelMapper modelMapper) {
		this.clienteService = clienteService;
        this.modelMapper = modelMapper;
	}
	
    //GET auth/login --> Return the view in path authentication/login
    @GetMapping("/login")
    public String login(Model model,
                        @RequestParam(name="error",required=false) String error,
                        @RequestParam(name="logout", required=false) String logout) {
        model.addAttribute("error", error);
        model.addAttribute("logout", logout);
        return ViewRouteHelper.USER_LOGIN;
    }

    //GET auth/loginSuccess --> Return the view in path home/index if login is successful
    @GetMapping("/loginSuccess")
    public String loginCheck() {
        return "redirect:/home/index";
    }
    
    
    @GetMapping("/register")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("cliente", new ClienteRequestDTO());
        return "authentication/register";
    }

    @PostMapping("/register")
    public String registrarCliente(@ModelAttribute("cliente") @Valid ClienteRequestDTO clienteDTO,
                                    BindingResult result,
                                    Model model) {

        if (result.hasErrors()) {
            return "authentication/register";
        }

        try {
            clienteService.save(clienteDTO); // Este método debe crear Cliente + User
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorDni", e.getMessage());
            return "authentication/register";
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("errorMessage", "Ya existe un cliente con ese email o DNI.");
            return "authentication/register";
        }

        return "redirect:/auth/login";
    }
}