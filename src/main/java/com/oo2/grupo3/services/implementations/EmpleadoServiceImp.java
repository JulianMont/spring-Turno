
package com.oo2.grupo3.services.implementations;



import java.text.MessageFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.oo2.grupo3.helpers.exceptions.EntidadNoEncontradaException;
import com.oo2.grupo3.helpers.exceptions.ErrorValidacionDatosException;
import com.oo2.grupo3.models.dtos.requests.EmpleadoRequestDTO;
import com.oo2.grupo3.models.dtos.responses.EmpleadoResponseDTO;
import com.oo2.grupo3.models.entities.Empleado;
import com.oo2.grupo3.models.entities.Especialidad;

import com.oo2.grupo3.models.entities.HorarioLaboral;
import com.oo2.grupo3.models.entities.UserEntity;
import com.oo2.grupo3.models.enums.DiaSemana;

import com.oo2.grupo3.models.enums.RoleType;
import com.oo2.grupo3.repositories.IEmpleadoRepository;
import com.oo2.grupo3.repositories.IEspecialidadRepository;


import com.oo2.grupo3.services.interfaces.IEmpleadoService;
import com.oo2.grupo3.services.interfaces.IUserService;




@Service
public class EmpleadoServiceImp implements IEmpleadoService {

	private final IEmpleadoRepository empleadoRepository;
	private final IEspecialidadRepository especialidadRepository;
	private final IUserService userService;
	private final ModelMapper modelMapper;
	
	public EmpleadoServiceImp(IEmpleadoRepository empleadoRepository,IEspecialidadRepository especialidadRepository,IUserService userService, ModelMapper modelMapper) {
		this.empleadoRepository = empleadoRepository;
		this.especialidadRepository = especialidadRepository;
		this.userService = userService;
		this.modelMapper = modelMapper;
	}

	@Override
	public Page<EmpleadoResponseDTO> findAll(Pageable pageable) {
		return empleadoRepository.findAll(pageable)
				.map(empleado-> modelMapper.map(empleado, EmpleadoResponseDTO.class));
	}

	@Override
	public Page<EmpleadoResponseDTO> findByIdEspecialidad(Long idEspecialidad, Pageable pageable) {
		return empleadoRepository.findByEspecialidad_IdEspecialidad(idEspecialidad, pageable)
				.map(empleado -> modelMapper.map(empleado, EmpleadoResponseDTO.class));
	}

	@Override
	public Page<EmpleadoResponseDTO> findByNombre(String nombre, Pageable pageable) {
		return empleadoRepository.findByNombreContainingIgnoreCase(nombre, pageable)
				.map(empleado -> modelMapper.map(empleado, EmpleadoResponseDTO.class));
	}

	@Override
	public EmpleadoResponseDTO findById(Integer id) {
		Empleado empleado = empleadoRepository.findById(id)
				.orElseThrow(() -> new EntidadNoEncontradaException("Empleado con id " + id + " no existe"));
		return modelMapper.map(empleado , EmpleadoResponseDTO.class);
	} 

	@Override
	public EmpleadoResponseDTO findByLegajo(String legajo) {
		Empleado empleado = empleadoRepository.findByLegajo(legajo)
				.orElseThrow(()-> new EntidadNoEncontradaException("Empleado con legajo " + legajo + " no existe"));
		return modelMapper.map(empleado, EmpleadoResponseDTO.class) ;
	}

	//TODO:Revisar el createEmpleado debido al UserEntity
	@Override
	public EmpleadoResponseDTO createEmpleado(EmpleadoRequestDTO empleadoRequestDTO) throws EntidadNoEncontradaException, ErrorValidacionDatosException{
		
		if(empleadoRepository.existsByLegajo(empleadoRequestDTO.getLegajo())) {
			 throw new ErrorValidacionDatosException("Ya existe un empleado con el legajo " + empleadoRequestDTO.getLegajo());
		}
		if(empleadoRepository.existsByDni(empleadoRequestDTO.getDni())){
			throw new ErrorValidacionDatosException("Ya existe un empleado con este DNI " + empleadoRequestDTO.getDni());
		}
		//especialidad existe?
		 Especialidad especialidad = especialidadRepository.findById(empleadoRequestDTO.getEspecialidadId())
			        .orElseThrow(() -> new EntidadNoEncontradaException("Especialidad con id " + empleadoRequestDTO.getEspecialidadId() + " no existe"));

		//seteo de datos
		Empleado empleado = modelMapper.map(empleadoRequestDTO, Empleado.class);
		empleado.setEspecialidad(especialidad);
		empleado.setUser(null); 

		//guardo en db para tener una id para el user
		empleado = empleadoRepository.save(empleado);
		
		
		
		 //TODO: Modificar asignacion automatica del rol
		 //La asignacion del role es temporal
		 //2° entrega agregar roles cliente,empleado y admin(gerente o algo asi)
		
		UserEntity user = userService.createUser(empleadoRequestDTO.getUser(),RoleType.ADMIN,empleado);

		
		empleado.setUser(user);
		Empleado empleadoCreado = empleadoRepository.save(empleado);
		return modelMapper.map(empleadoCreado, EmpleadoResponseDTO.class);
	}

	@Override
	public EmpleadoResponseDTO actualizarEmpleado(Integer idEmpleado, EmpleadoRequestDTO empleadoRequestDTO) throws EntidadNoEncontradaException, ErrorValidacionDatosException{
		
		Empleado empleadoExiste = empleadoRepository.findById(idEmpleado)
		        .orElseThrow(() -> new EntidadNoEncontradaException(MessageFormat.format("Empleado con id {0} no existe", idEmpleado)));
		
		//legajo en uso
		Optional<Empleado> posibleDuplicado = empleadoRepository.findByLegajo(empleadoRequestDTO.getLegajo());
		if (posibleDuplicado.isPresent() && !posibleDuplicado.get().getIdPersona().equals(idEmpleado)) {
			throw new ErrorValidacionDatosException("Ya existe otro empleado con el legajo " + empleadoRequestDTO.getLegajo());
		}
		
		//especialidad existe?
		
	    Especialidad especialidad = especialidadRepository.findById(empleadoRequestDTO.getEspecialidadId())
	            .orElseThrow(() -> new EntidadNoEncontradaException("Especialidad con id " + empleadoRequestDTO.getEspecialidadId() + " no existe"));

		//seteo de datos
		modelMapper.map(empleadoRequestDTO, empleadoExiste);
        empleadoExiste.setEspecialidad(especialidad);
        
        // actualizar user
        if (empleadoExiste.getUser() != null && empleadoRequestDTO.getUser() != null) {
            userService.updateUser(empleadoExiste.getUser().getId(), empleadoRequestDTO.getUser());
        }
        
        
		Empleado empleadoActualizado = empleadoRepository.save(empleadoExiste);
		
		return modelMapper.map(empleadoActualizado, EmpleadoResponseDTO.class);
	}

	@Override
	public boolean borrarEmpleado(Integer idEmpleado) throws EntidadNoEncontradaException, ErrorValidacionDatosException{
		Empleado empleadoExiste = empleadoRepository.findById(idEmpleado)
				.orElseThrow(() -> new EntidadNoEncontradaException(MessageFormat.format("Empleado con id {0} no existe",idEmpleado)));
		
//		if(empleadoExiste.getUser() != null) {
//			userService.deleteUser(empleadoExiste.getUser().getId());
//		}
		
		empleadoRepository.delete(empleadoExiste);
		return true;
	}

	@Override
	public Object getAll() {
		// TODO Auto-generated method stub
		return null;
	}
	

	@Override
	public List<Empleado> getAllEmpleados() {
	    return empleadoRepository.findAll();
	}

	@Override
	public Page<EmpleadoResponseDTO> buscarEmpleadosFiltrados(String nombre, String legajo, Long especialidadId,
			Pageable pageable) {
		// TODO Auto-generated method stub
		
		// preguntar a los profes como cambiar esto
	    // fix para que lleguen null a la db
	    if (nombre != null && nombre.isBlank()) {
	        nombre = null;
	    }
	    if (legajo != null && legajo.isBlank()) {
	        legajo = null;
	    }
	    if (especialidadId != null && especialidadId == 0) {
	        especialidadId = null;
	    }

		return empleadoRepository.buscarEmpleadosFiltrados(nombre, legajo, especialidadId, pageable)
				.map(empleado -> modelMapper.map(empleado, EmpleadoResponseDTO.class));
	}

	
	public List<LocalDate> getDiasDisponiblesParaEmpleado(Integer idEmpleado, LocalDate desde, LocalDate hasta) {
	    Empleado empleado = empleadoRepository.findById(idEmpleado)
	            .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));

	    // Suponiendo que querés un rango de fechas desde->hasta
	    List<LocalDate> diasDisponibles = new ArrayList<>();

	    for (LocalDate fecha = desde; !fecha.isAfter(hasta); fecha = fecha.plusDays(1)) {
	        DiaSemana diaEnum = fromJavaDayOfWeek(fecha.getDayOfWeek());
	        boolean trabajaEseDia = empleado.getHorariosLaborales().stream()
	        	    .anyMatch(hl -> hl.getDiaSemana().equals(diaEnum));
	        if (trabajaEseDia) {
	            diasDisponibles.add(fecha);
	        }
	    }
	    return diasDisponibles;
	}

	public List<LocalTime> getHorasDisponiblesParaEmpleadoEnDia(Integer idEmpleado, LocalDate fecha) {
	    Empleado empleado = empleadoRepository.findById(idEmpleado)
	            .orElseThrow(() -> new RuntimeException("Empleado no encontrado"));

	    DiaSemana diaEnum = fromJavaDayOfWeek(fecha.getDayOfWeek());

	    // Buscar horarios laborales para ese día
	    List<HorarioLaboral> horariosDia = empleado.getHorariosLaborales().stream()
	        .filter(hl -> hl.getDiaSemana().equals(diaEnum))
	        .collect(Collectors.toList());

	    List<LocalTime> horasDisponibles = new ArrayList<>();
	    for (HorarioLaboral hl : horariosDia) {
	        LocalTime hora = hl.getHoraInicio();
	        while (!hora.isAfter(hl.getHoraFin())) {
	            horasDisponibles.add(hora);
	            hora = hora.plusMinutes(30); // asumiendo intervalos de 30 min
	        }
	    }

	    return horasDisponibles;
	}


	
	
	private DiaSemana fromJavaDayOfWeek(DayOfWeek dayOfWeek) {
	    return switch (dayOfWeek) {
	        case MONDAY -> DiaSemana.LUNES;
	        case TUESDAY -> DiaSemana.MARTES;
	        case WEDNESDAY -> DiaSemana.MIERCOLES;
	        case THURSDAY -> DiaSemana.JUEVES;
	        case FRIDAY -> DiaSemana.VIERNES;
	        case SATURDAY -> DiaSemana.SABADO;
	        case SUNDAY -> DiaSemana.DOMINGO;
	    };
	}
	
	
}
