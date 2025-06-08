
package com.oo2.grupo3.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
import com.oo2.grupo3.models.dtos.requests.EmpleadoRequestDTO;
import com.oo2.grupo3.models.dtos.responses.EmpleadoResponseDTO;
import com.oo2.grupo3.services.interfaces.IEmpleadoService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/empleados")
public class EmpleadoController {

	private final IEmpleadoService empleadoService;
	
	public EmpleadoController(IEmpleadoService empleadoService) {
		this.empleadoService = empleadoService;
	}
	
	
	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping
    public String listarEmpleados(Model model, @PageableDefault(size = 10) Pageable pageable) {
        Page<EmpleadoResponseDTO> empleados = empleadoService.findAll(pageable);
        model.addAttribute("empleados", empleados);
        return ViewRouteHelper.EMPLEADO_LIST;
    }
	
	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	@GetMapping("/buscar/nombre")
	public String buscarPorNombre(@RequestParam("nombre") String nombre,
	                              @PageableDefault(size = 10) Pageable pageable,
	                              Model model) {
	    Page<EmpleadoResponseDTO> empleados = empleadoService.findByNombre(nombre, pageable);
	    model.addAttribute("empleados", empleados);
	    return ViewRouteHelper.EMPLEADO_LIST;
	}

	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
	@GetMapping("/buscar/especialidad")
	public String buscarPorEspecialidad(@RequestParam("idEspecialidad") Long idEspecialidad,
	                                    @PageableDefault(size = 10) Pageable pageable,
	                                    Model model) {
	    Page<EmpleadoResponseDTO> empleados = empleadoService.findByIdEspecialidad(idEspecialidad, pageable);
	    model.addAttribute("empleados", empleados);
	    return ViewRouteHelper.EMPLEADO_LIST;
	}


    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping("/detalle/{id}")
    public String verDetalle(@PathVariable Integer id, Model model) {
        EmpleadoResponseDTO empleado = empleadoService.findById(id);
        model.addAttribute("empleado", empleado);
        return ViewRouteHelper.EMPLEADO_DETALLE;
    }

    // --- SOLO ADMIN ---

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        model.addAttribute("empleadoRequestDTO", new EmpleadoRequestDTO());
        return ViewRouteHelper.EMPLEADO_NUEVO;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/nuevo")
    public String crearEmpleado(@Valid @ModelAttribute("empleadoRequestDTO") EmpleadoRequestDTO dto,
                                BindingResult result,
                                Model model) {
        if (result.hasErrors()) {
            return ViewRouteHelper.EMPLEADO_NUEVO;
        }

        try {
            empleadoService.createEmpleado(dto);
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorLegajo", e.getMessage());
            return ViewRouteHelper.EMPLEADO_NUEVO;
        }

        return ViewRouteHelper.REDIRECT_EMPLEADOS;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Integer id, Model model) {
        EmpleadoResponseDTO empleado = empleadoService.findById(id);
        model.addAttribute("empleadoRequestDTO", empleado);
        return ViewRouteHelper.EMPLEADO_EDITAR;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/editar/{id}")
    public String actualizarEmpleado(@PathVariable Integer id,
                                     @Valid @ModelAttribute("empleadoRequestDTO") EmpleadoRequestDTO dto,
                                     BindingResult result,
                                     Model model) {
        if (result.hasErrors()) {
            return ViewRouteHelper.EMPLEADO_EDITAR;
        }

        try {
            empleadoService.actualizarEmpleado(id, dto);
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorLegajo", e.getMessage());
            return ViewRouteHelper.EMPLEADO_EDITAR;
        }

        return ViewRouteHelper.REDIRECT_EMPLEADOS;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/eliminar/{id}")
    public String eliminarEmpleado(@PathVariable Integer id) {
        empleadoService.borrarEmpleado(id);
        return ViewRouteHelper.REDIRECT_EMPLEADOS;
    }
	
}