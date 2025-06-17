package com.oo2.grupo3.controllers;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.oo2.grupo3.helpers.ViewRouteHelper;
import com.oo2.grupo3.models.dtos.requests.EspecialidadRequestDTO;
import com.oo2.grupo3.models.dtos.responses.EspecialidadResponseDTO;
import com.oo2.grupo3.services.interfaces.IEspecialidadService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/especialidades")
public class EspecialidadController {

    private final IEspecialidadService especialidadService;

    public EspecialidadController(IEspecialidadService especialidadService) {
        this.especialidadService = especialidadService;
    }
    
    @GetMapping("list")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public String listaEspecialidades(Model model) {
    	
        List<EspecialidadResponseDTO> especialidades = especialidadService.traerEspecialidades();
        model.addAttribute("especialidades", especialidades);
        
        return ViewRouteHelper.ESPECIALIDAD_LISTA;
    }
    
    @GetMapping("/form")
    @PreAuthorize("hasRole('ADMIN')")
    public String createForm(Model model) {
    	
        model.addAttribute("especialidadRequestDTO", new EspecialidadRequestDTO());
        
        return ViewRouteHelper.ESPECIALIDAD_FORM;
    }
    
    @PostMapping("/save")
    @PreAuthorize("hasRole('ADMIN')")
    public String guardarEspecialidad(@Valid @ModelAttribute EspecialidadRequestDTO especialidadRequestDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return ViewRouteHelper.ESPECIALIDAD_FORM;
        }
        try {
            especialidadService.crearEspecialidad(especialidadRequestDTO);
        } catch (IllegalArgumentException e) {
    
        	model.addAttribute("errorMessage", e.getMessage());
            return ViewRouteHelper.ESPECIALIDAD_FORM;
        }

        return ViewRouteHelper.REDIRECT_ESPECIALIDAD_LISTA;
    }
    
    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String editForm(@PathVariable Long id, Model model) {
    	
        EspecialidadResponseDTO dto = especialidadService.findById(id);
        EspecialidadRequestDTO requestDTO = new EspecialidadRequestDTO();
        
        requestDTO.setNombre(dto.getNombre());

        model.addAttribute("especialidadRequestDTO", requestDTO);
        model.addAttribute("id", id);

        return ViewRouteHelper.ESPECIALIDAD_FORM;
    }
    
    @PostMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String update(@PathVariable Long id, @Valid @ModelAttribute EspecialidadRequestDTO especialidadRequestDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return ViewRouteHelper.ESPECIALIDAD_FORM;
        }
        
        try {
            especialidadService.editarEspecialidad(id, especialidadRequestDTO);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error al actualizar la especialidad: " + e.getMessage());
            return ViewRouteHelper.ESPECIALIDAD_FORM;
        }
         
        return ViewRouteHelper.REDIRECT_ESPECIALIDAD_LISTA;
    }
    
    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String delete(@PathVariable Long id,Model model) {
        try {
            especialidadService.borrarEspecialidad(id);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "No se pudo eliminar la especialidad: " + e.getMessage());
        }
        return ViewRouteHelper.REDIRECT_ESPECIALIDAD_LISTA;
    }
    
    
    
    
    
}
