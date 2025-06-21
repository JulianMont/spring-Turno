
package com.oo2.grupo3.controllers;


import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
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
import org.springframework.web.bind.annotation.ResponseBody;

import com.oo2.grupo3.helpers.ViewRouteHelper;
import com.oo2.grupo3.helpers.exceptions.EntidadNoEncontradaException;
import com.oo2.grupo3.helpers.exceptions.ErrorValidacionDatosException;
import com.oo2.grupo3.models.dtos.requests.EmpleadoRequestDTO;
import com.oo2.grupo3.models.dtos.responses.EmpleadoResponseDTO;


import com.oo2.grupo3.models.dtos.responses.EspecialidadResponseDTO;
import com.oo2.grupo3.models.dtos.responses.HorarioLaboralResponseDTO;
import com.oo2.grupo3.services.interfaces.IEmpleadoService;
import com.oo2.grupo3.services.interfaces.IEspecialidadService;
import com.oo2.grupo3.services.interfaces.IHorarioLaboralService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/empleados")
public class EmpleadoController {

	private final IEmpleadoService empleadoService;
	private final IHorarioLaboralService horarioLaboralService;

  
	
    private final IEspecialidadService especialidadService;
    private final ModelMapper modelMapper;
	
    public EmpleadoController(
            IEmpleadoService empleadoService,
            IEspecialidadService especialidadService,
            ModelMapper modelMapper,
            IHorarioLaboralService horarioLaboralService) {
        this.empleadoService = empleadoService;
        this.especialidadService = especialidadService;
        this.modelMapper = modelMapper;
        this.horarioLaboralService = horarioLaboralService;
    }
	
    //CHECK
	@PreAuthorize("hasRole('CLIENTE') or hasRole('ADMIN') or hasRole('EMPLEADO')")
	@GetMapping("/empleados/{idEmpleado}/horarios")
	public List<HorarioLaboralResponseDTO> getHorariosEmpleado(@PathVariable Integer idEmpleado) {
		EmpleadoResponseDTO empleado = empleadoService.findById(idEmpleado);
	    List<HorarioLaboralResponseDTO> horarios = empleado.getHorariosLaborales()
	            .stream()
	            .map(hl -> modelMapper.map(hl, HorarioLaboralResponseDTO.class))
	            .collect(Collectors.toList());
	    return horarios;
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
		
		
        Page<EmpleadoResponseDTO> empleados = empleadoService.buscarEmpleadosFiltrados(nombre, legajo, especialidadId, pageable);

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

    // --- SOLO ADMIN ---

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/form")
    public String mostrarFormularioCrear(Model model) {
        model.addAttribute("empleadoRequestDTO", new EmpleadoRequestDTO());
        model.addAttribute("especialidades", especialidadService.traerEspecialidades());
        return ViewRouteHelper.EMPLEADOS_FORM;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/save")
    public String crearEmpleado(@Valid @ModelAttribute("empleadoRequestDTO") EmpleadoRequestDTO dto,
                                BindingResult result,
                                Model model) {
        if (result.hasErrors()) {
        	model.addAttribute("especialidades", especialidadService.traerEspecialidades());
            return ViewRouteHelper.EMPLEADOS_FORM;
        }

        
        try {
            empleadoService.createEmpleado(dto);
        } catch (EntidadNoEncontradaException | ErrorValidacionDatosException e) {
            model.addAttribute("errorMessage", e.getMessage());
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

        model.addAttribute("empleadoRequestDTO", requestDTO);
        model.addAttribute("especialidades", especialidadService.traerEspecialidades());
        return ViewRouteHelper.EMPLEADOS_FORM;
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/editar/{id}")
    public String editarEmpleado(@PathVariable Integer id,
                                 @ModelAttribute("empleadoRequestDTO") @Valid EmpleadoRequestDTO empleadoRequestDTO,
                                 BindingResult result,
                                 Model model) throws EntidadNoEncontradaException, ErrorValidacionDatosException {
        if (result.hasErrors()) {
            model.addAttribute("especialidades", especialidadService.traerEspecialidades());
            return ViewRouteHelper.EMPLEADOS_FORM;
        }

        try {
            empleadoService.actualizarEmpleado(id, empleadoRequestDTO);
        } catch (EntidadNoEncontradaException | ErrorValidacionDatosException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("especialidades", especialidadService.traerEspecialidades());
            return ViewRouteHelper.EMPLEADOS_FORM;
        }
        
        return ViewRouteHelper.REDIRECT_EMPLEADOS_LIST;
    }
   
    
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/eliminar/{id}")

    public String eliminarEmpleado(@PathVariable Integer id,Model model) {
        empleadoService.borrarEmpleado(id);
        return ViewRouteHelper.REDIRECT_EMPLEADOS_LIST;
    }


}

