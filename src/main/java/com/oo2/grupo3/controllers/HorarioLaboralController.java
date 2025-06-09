package com.oo2.grupo3.controllers;

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

        horarioLaboralService.agregarHorario(idEmpleado, dto);
        return ViewRouteHelper.EMPLEADOS_DETALLE_REDIRECT + idEmpleado;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{idHorario}/editar")
    public String mostrarFormularioEditar(@PathVariable Integer idEmpleado,
                                          @PathVariable Integer idHorario,
                                          Model model) {
        HorarioLaboralResponseDTO dto = horarioLaboralService.findbyId(idHorario);
        model.addAttribute("horarioLaboral", dto);
        model.addAttribute("idEmpleado", idEmpleado);
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

        horarioLaboralService.editarHorario(idEmpleado, idHorario, dto);
        return ViewRouteHelper.EMPLEADOS_DETALLE_REDIRECT + idEmpleado;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{idHorario}/eliminar")
    public String eliminarHorario(@PathVariable Integer idEmpleado,
                                  @PathVariable Integer idHorario) {
        horarioLaboralService.eliminarHorario(idEmpleado, idHorario);
        return ViewRouteHelper.EMPLEADOS_DETALLE_REDIRECT + idEmpleado;
    }
}
