package com.oo2.grupo3.services.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.oo2.grupo3.models.dtos.requests.EmpleadoRequestDTO;
import com.oo2.grupo3.models.dtos.responses.EmpleadoResponseDTO;


public interface IEmpleadoService {
	
	//Lo usa cualquier usuario
	
	Page<EmpleadoResponseDTO> findAll(Pageable pageable);
	
	Page<EmpleadoResponseDTO> findByIdEspecialidad(Long idEspecialidad , Pageable pageable);

	Page<EmpleadoResponseDTO> findByName(String nombre , Pageable pageable);
	
	EmpleadoResponseDTO findById(Integer id);
	
	
	//admin
	//lo usa la parte de gestion de empleado
	EmpleadoResponseDTO findByLegajo(String legajo);
	
	EmpleadoResponseDTO createEmpleado(EmpleadoRequestDTO empleadoRequestDTO);
	EmpleadoResponseDTO actualizarEmpleado(Integer idEmpleado,EmpleadoRequestDTO empleadoRequestDTO);
	boolean borrarEmpleado(Integer idEmpleado);


}
