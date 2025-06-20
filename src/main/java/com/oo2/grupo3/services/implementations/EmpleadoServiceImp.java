package com.oo2.grupo3.services.implementations;



import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.oo2.grupo3.models.dtos.requests.EmpleadoRequestDTO;
import com.oo2.grupo3.models.dtos.responses.EmpleadoResponseDTO;
import com.oo2.grupo3.models.entities.Empleado;
import com.oo2.grupo3.models.entities.Especialidad;
import com.oo2.grupo3.models.entities.UserEntity;
import com.oo2.grupo3.models.enums.RoleType;
import com.oo2.grupo3.repositories.IEmpleadoRepository;
import com.oo2.grupo3.repositories.IEspecialidadRepository;


import com.oo2.grupo3.services.interfaces.IEmpleadoService;
import com.oo2.grupo3.services.interfaces.IUserService;

import jakarta.persistence.EntityNotFoundException;


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
				.orElseThrow(() -> new EntityNotFoundException("Empleado con id " + id + " no existe"));
		return modelMapper.map(empleado , EmpleadoResponseDTO.class);
	} 

	@Override
	public EmpleadoResponseDTO findByLegajo(String legajo) {
		Empleado empleado = empleadoRepository.findByLegajo(legajo)
				.orElseThrow(()-> new EntityNotFoundException("Empleado con legajo " + legajo + " no existe"));
		return modelMapper.map(empleado, EmpleadoResponseDTO.class) ;
	}

	@Override
	public EmpleadoResponseDTO createEmpleado(EmpleadoRequestDTO empleadoRequestDTO){
		
		if(empleadoRepository.existsByLegajo(empleadoRequestDTO.getLegajo())) {
			 throw new IllegalArgumentException("Ya existe un empleado con el legajo " + empleadoRequestDTO.getLegajo());
		}
		
		//especialidad existe?
		 Especialidad especialidad = especialidadRepository.findById(empleadoRequestDTO.getEspecialidadId())
			        .orElseThrow(() -> new EntityNotFoundException("Especialidad con id " + empleadoRequestDTO.getEspecialidadId() + " no existe"));
		
		 //TODO: Modificar asignacion automatica del rol
		 //La asignacion del role es temporal
		 //2° entrega agregar roles cliente,empleado y admin(gerente o algo asi)
		 UserEntity user = userService.createUser(empleadoRequestDTO.getUser(),RoleType.ADMIN);
		 
		 
		//seteo de datos
		Empleado empleado = modelMapper.map(empleadoRequestDTO, Empleado.class);
		empleado.setEspecialidad(especialidad);
		empleado.setUser(user);
		Empleado empleadoCreado = empleadoRepository.save(empleado);
		return modelMapper.map(empleadoCreado, EmpleadoResponseDTO.class);
	}

	@Override
	public EmpleadoResponseDTO actualizarEmpleado(Integer idEmpleado, EmpleadoRequestDTO empleadoRequestDTO) throws Exception {
		
		Empleado empleadoExiste = empleadoRepository.findById(idEmpleado)
		        .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Empleado con id {0} no existe", idEmpleado)));
		
		//legajo en uso
		Optional<Empleado> posibleDuplicado = empleadoRepository.findByLegajo(empleadoRequestDTO.getLegajo());
		if (posibleDuplicado.isPresent() && !posibleDuplicado.get().getIdPersona().equals(idEmpleado)) {
			throw new IllegalArgumentException("Ya existe otro empleado con el legajo " + empleadoRequestDTO.getLegajo());
		}
		
		//especialidad existe?
		
	    Especialidad especialidad = especialidadRepository.findById(empleadoRequestDTO.getEspecialidadId())
	            .orElseThrow(() -> new EntityNotFoundException("Especialidad con id " + empleadoRequestDTO.getEspecialidadId() + " no existe"));

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
	public boolean borrarEmpleado(Integer idEmpleado) throws Exception {
		Empleado empleadoExiste = empleadoRepository.findById(idEmpleado)
				.orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Empleado con id {0} no existe",idEmpleado)));
		
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

}
