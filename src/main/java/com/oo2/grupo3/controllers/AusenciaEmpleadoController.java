package com.oo2.grupo3.controllers;



import java.time.LocalDate;

import org.modelmapper.ModelMapper;
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
import com.oo2.grupo3.models.dtos.requests.AusenciaEmpleadoRequestDTO;
import com.oo2.grupo3.models.dtos.responses.AusenciaEmpleadoResponseDTO;

import com.oo2.grupo3.services.interfaces.IAusenciaEmpleadoService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/empleados/{idEmpleado}/ausencias")
public class AusenciaEmpleadoController {

    private final IAusenciaEmpleadoService ausenciaService;
    private final ModelMapper modelMapper;
    
    public AusenciaEmpleadoController(IAusenciaEmpleadoService ausenciaService,ModelMapper modelMapper) {
        this.ausenciaService = ausenciaService;
        this.modelMapper = modelMapper;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/form")
    public String mostrarFormularioCrearAusencia(@PathVariable Integer idEmpleado, Model model) {
        model.addAttribute("ausenciaRequestDTO",  new AusenciaEmpleadoRequestDTO());
        model.addAttribute("idEmpleado", idEmpleado);
        
//        para permitir que solamente se puedan registrar fechas con 7 dias de retraso
        LocalDate fechaMin = LocalDate.now().minusDays(7);
        model.addAttribute("fechaMin", fechaMin);
        
        return ViewRouteHelper.AUSENCIA_FORM;
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/save")
    public String guardarNuevaAusencia(@PathVariable Integer idEmpleado,
                                       @ModelAttribute @Valid AusenciaEmpleadoRequestDTO ausenciaRequestDTO,
                                       BindingResult result,
                                       Model model) {
        if (result.hasErrors()) {
            model.addAttribute("idEmpleado", idEmpleado);
            model.addAttribute("ausenciaRequestDTO", ausenciaRequestDTO);
            return ViewRouteHelper.AUSENCIA_FORM;
        }

        try {
            ausenciaService.agregarAusencia(idEmpleado, ausenciaRequestDTO);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error al guardar la ausencia: " + e.getMessage());
            model.addAttribute("idEmpleado", idEmpleado);
            model.addAttribute("ausenciaRequestDTO", ausenciaRequestDTO);
            return ViewRouteHelper.AUSENCIA_FORM;
        }
        
        //TODO:CAMBIAR POR METODO
        return ViewRouteHelper.EMPLEADOS_DETALLE_REDIRECT + idEmpleado;
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{idAusencia}/editar")
    public String mostrarFormularioEditar(@PathVariable Integer idEmpleado,
                                          @PathVariable Integer idAusencia,
                                          Model model) {
    	

      	
    	
    	 try {
//    	      para permitir que solamente se pueda modificar fechas con 7 dias de retraso
    	      LocalDate fechaMin = LocalDate.now().minusDays(7);
             AusenciaEmpleadoResponseDTO dtoResp = ausenciaService.findbyId(idAusencia);
             AusenciaEmpleadoRequestDTO ausenciaRequestDTO = modelMapper.map(dtoResp,AusenciaEmpleadoRequestDTO.class);
             model.addAttribute("ausenciaRequestDTO", ausenciaRequestDTO);
             model.addAttribute("idEmpleado", idEmpleado);
             model.addAttribute("fechaMin", fechaMin);
         } catch (Exception e) {
             model.addAttribute("errorMessage", "Error al cargar la ausencia: " + e.getMessage());
             return ViewRouteHelper.EMPLEADOS_DETALLE_REDIRECT + idEmpleado;
         }
        return ViewRouteHelper.AUSENCIA_FORM;
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{idAusencia}/actualizar")
    public String actualizarAusencia(@PathVariable Integer idEmpleado,
                                     @PathVariable Integer idAusencia,
                                     @ModelAttribute @Valid AusenciaEmpleadoRequestDTO ausenciaRequestDTO,
                                     BindingResult result,
                                     Model model) {
        if (result.hasErrors()) {
            model.addAttribute("idEmpleado", idEmpleado);
            model.addAttribute("ausenciaRequestDTO", ausenciaRequestDTO);
            return ViewRouteHelper.AUSENCIA_FORM;
        }

        try {
            ausenciaService.modificarAusencia(idEmpleado, idAusencia, ausenciaRequestDTO);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error al actualizar la ausencia: " + e.getMessage());
            model.addAttribute("idEmpleado", idEmpleado);
            model.addAttribute("ausenciaRequestDTO", ausenciaRequestDTO);
            return ViewRouteHelper.AUSENCIA_FORM;
        }
        return ViewRouteHelper.EMPLEADOS_DETALLE_REDIRECT + idEmpleado;
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{idAusencia}/eliminar")
    public String eliminarAusencia(@PathVariable Integer idEmpleado,
                                   @PathVariable Integer idAusencia,Model model) {
        try {
            ausenciaService.eliminarAusencia(idEmpleado, idAusencia);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error al eliminar la ausencia: " + e.getMessage());
        }

        return ViewRouteHelper.EMPLEADOS_DETALLE_REDIRECT + idEmpleado;
    }
    
    
    
}
