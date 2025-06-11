
package com.oo2.grupo3.controllers;


import java.util.List;

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
import com.oo2.grupo3.models.dtos.requests.EmpleadoRequestDTO;
import com.oo2.grupo3.models.dtos.responses.EmpleadoResponseDTO;


import com.oo2.grupo3.models.dtos.responses.EspecialidadResponseDTO;
import com.oo2.grupo3.services.interfaces.IEmpleadoService;
import com.oo2.grupo3.services.interfaces.IEspecialidadService;


import jakarta.validation.Valid;

@Controller
@RequestMapping("/empleados")
public class EmpleadoController {

	private final IEmpleadoService empleadoService;

  
    private final IEspecialidadService especialidadService;
    private final ModelMapper modelMapper;
	
	public EmpleadoController(IEmpleadoService empleadoService,IEspecialidadService especialidadService,ModelMapper modelMapper) {
		this.empleadoService = empleadoService;
        this.especialidadService = especialidadService;
        this.modelMapper = modelMapper;

	}
	
	
	@PreAuthorize("hasAnyRole('USER', 'ADMIN')")

    @GetMapping("/list")
    public String listarEmpleados(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String legajo,
            @RequestParam(required = false) Long especialidadId,
            Pageable pageable,
            Model model
    ) {
        Page<EmpleadoResponseDTO> empleados;

        //filtro de busqueda
        
        if (legajo != null && !legajo.isBlank()) {
            EmpleadoResponseDTO empleado = empleadoService.findByLegajo(legajo);
            empleados = new PageImpl<>(List.of(empleado));
        } else if (nombre != null && !nombre.isBlank()) {
            empleados = empleadoService.findByNombre(nombre, pageable);
        } else if (especialidadId != null) {
            empleados = empleadoService.findByIdEspecialidad(especialidadId, pageable);
        } else {
            empleados = empleadoService.findAll(pageable);
        }

        List<EspecialidadResponseDTO> especialidades = especialidadService.traerEspecialidades();

        model.addAttribute("empleados", empleados);
        model.addAttribute("especialidades", especialidades);
        model.addAttribute("nombre", nombre);
        model.addAttribute("legajo", legajo);
        model.addAttribute("especialidadId", especialidadId);

        
        return ViewRouteHelper.EMPLEADOS_LIST;
        
    }
	
    @GetMapping("/{id}")
    public String verDetalleEmpleado(@PathVariable Integer id, Model model) {
    	
        EmpleadoResponseDTO empleado = empleadoService.findById(id);
        model.addAttribute("empleado", empleado);
        
        return ViewRouteHelper.EMPLEADOS_DETALLE;
    }
    

	


//    // --- SOLO ADMIN ---

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/form")
    public String mostrarFormularioCrear(Model model) {
        model.addAttribute("empleadoRequestDTO", new EmpleadoRequestDTO());
        model.addAttribute("especialidades", especialidadService.traerEspecialidades());
        return ViewRouteHelper.EMPLEADOS_FORM;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/save")
    public String crearEmpleado(@Valid @ModelAttribute EmpleadoRequestDTO dto,
                                BindingResult result,
                                Model model) {
        if (result.hasErrors()) {
        	model.addAttribute("especialidades", especialidadService.traerEspecialidades());
            return ViewRouteHelper.EMPLEADOS_FORM;

          
        }

        try {
            empleadoService.createEmpleado(dto);
        } catch (IllegalArgumentException e) {

        	
            model.addAttribute("errorLegajo", e.getMessage());
            model.addAttribute("especialidades", especialidadService.traerEspecialidades());
            return ViewRouteHelper.EMPLEADOS_FORM;
            
        }

        return ViewRouteHelper.REDIRECT_EMPLEADOS_LIST;
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Integer id, Model model) {
        EmpleadoResponseDTO empleado = empleadoService.findById(id);

        
        //TODO: CHECKEAR ESTO
        EmpleadoRequestDTO requestDTO = modelMapper.map(empleado, EmpleadoRequestDTO.class);

        model.addAttribute("empleado", requestDTO);
        model.addAttribute("especialidades", especialidadService.traerEspecialidades());
        return ViewRouteHelper.EMPLEADOS_FORM;
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/editar/{id}")
    public String editarEmpleado(@PathVariable Integer id, @ModelAttribute("empleado") @Valid EmpleadoRequestDTO empleadoRequestDTO, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("especialidades", especialidadService.traerEspecialidades());
            return ViewRouteHelper.EMPLEADOS_FORM;
        }

        empleadoService.actualizarEmpleado(id, empleadoRequestDTO);
        return ViewRouteHelper.REDIRECT_EMPLEADOS_LIST;
    }
    
  
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/eliminar/{id}")
    public String eliminarEmpleado(@PathVariable Integer id) {
        empleadoService.borrarEmpleado(id);

        return ViewRouteHelper.REDIRECT_EMPLEADOS_LIST;
    }


}
	

