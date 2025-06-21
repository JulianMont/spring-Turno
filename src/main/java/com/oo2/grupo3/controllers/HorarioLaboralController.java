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
import org.springframework.web.bind.annotation.ResponseBody;

import com.oo2.grupo3.helpers.ViewRouteHelper;
import com.oo2.grupo3.models.dtos.requests.HorarioLaboralRequestDTO;
import com.oo2.grupo3.models.dtos.responses.HorarioLaboralResponseDTO;
import com.oo2.grupo3.services.interfaces.IHorarioLaboralService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/empleados/{idEmpleado}/horarios")
public class HorarioLaboralController {

    private final IHorarioLaboralService horarioLaboralService;

    public HorarioLaboralController(IHorarioLaboralService horarioLaboralService) {
        this.horarioLaboralService = horarioLaboralService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/form")
    public String mostrarFormularioCrearHorario(@PathVariable Integer idEmpleado, Model model) {
        model.addAttribute("horarioLaboral", new HorarioLaboralRequestDTO());
        model.addAttribute("idEmpleado", idEmpleado);
        return ViewRouteHelper.HORARIO_FORM;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/save")
    public String guardarNuevoHorario(@PathVariable Integer idEmpleado,
                                      @ModelAttribute("horarioLaboral") @Valid HorarioLaboralRequestDTO dto,
                                      BindingResult result,
                                      Model model) {
        if (result.hasErrors()) {
            model.addAttribute("idEmpleado", idEmpleado);
            return ViewRouteHelper.HORARIO_FORM;
        }

        try {
            horarioLaboralService.agregarHorario(idEmpleado, dto);
        } catch (Exception e) {
            model.addAttribute("idEmpleado", idEmpleado);
            model.addAttribute("errorMessage", "Error al guardar el horario: " + e.getMessage());
            return ViewRouteHelper.HORARIO_FORM;
        }
        
        return ViewRouteHelper.EMPLEADOS_DETALLE_REDIRECT + idEmpleado;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{idHorario}/editar")
    public String mostrarFormularioEditar(@PathVariable Integer idEmpleado,
                                          @PathVariable Integer idHorario,
                                          Model model) {
        try {
            HorarioLaboralResponseDTO dto = horarioLaboralService.findbyId(idHorario);
            model.addAttribute("horarioLaboral", dto);
            model.addAttribute("idEmpleado", idEmpleado);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error al cargar el horario laboral: " + e.getMessage());
            return ViewRouteHelper.EMPLEADOS_DETALLE_REDIRECT + idEmpleado;
        }
        
        return ViewRouteHelper.HORARIO_FORM;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{idHorario}/actualizar")
    public String actualizarHorario(@PathVariable Integer idEmpleado,
                                    @PathVariable Integer idHorario,
                                    @ModelAttribute("horarioLaboral") @Valid HorarioLaboralRequestDTO dto,
                                    BindingResult result,
                                    Model model) {
        if (result.hasErrors()) {
            model.addAttribute("idEmpleado", idEmpleado);
            return ViewRouteHelper.HORARIO_FORM;
        }

        try {
            horarioLaboralService.editarHorario(idEmpleado, idHorario, dto);
        } catch (Exception e) {
            model.addAttribute("idEmpleado", idEmpleado);
            model.addAttribute("errorMessage", "Error al actualizar el horario: " + e.getMessage());
            return ViewRouteHelper.HORARIO_FORM;
        }
        
        return ViewRouteHelper.EMPLEADOS_DETALLE_REDIRECT + idEmpleado;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{idHorario}/eliminar")
    public String eliminarHorario(@PathVariable Integer idEmpleado,
                                  @PathVariable Integer idHorario,Model model) {
        try {
            horarioLaboralService.eliminarHorario(idEmpleado, idHorario);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error al eliminar el horario: " + e.getMessage());
        }
        return ViewRouteHelper.EMPLEADOS_DETALLE_REDIRECT + idEmpleado;
    }
    
    @PreAuthorize("hasRole('CLIENTE') or hasRole('ADMIN') or hasRole('EMPLEADO')")
    @GetMapping
    @ResponseBody
    public List<HorarioLaboralResponseDTO> obtenerHorariosLaboralesDelEmpleado(@PathVariable Integer idEmpleado) {
        return horarioLaboralService.obtenerHorariosDelEmpleado(idEmpleado);
    }
    
    
}
