package com.oo2.grupo3.services.interfaces;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.oo2.grupo3.models.dtos.requests.EmpleadoRequestDTO;
import com.oo2.grupo3.models.dtos.responses.EmpleadoResponseDTO;
import com.oo2.grupo3.models.entities.Empleado;


public interface IEmpleadoService {
	

	
	Page<EmpleadoResponseDTO> findAll(Pageable pageable);
	
	Page<EmpleadoResponseDTO> findByIdEspecialidad(Long idEspecialidad , Pageable pageable);

	Page<EmpleadoResponseDTO> findByNombre(String nombre , Pageable pageable);
	
	EmpleadoResponseDTO findById(Integer id);
	
	List<Empleado> getAll();
	
	//admin

	EmpleadoResponseDTO findByLegajo(String legajo);
	
	EmpleadoResponseDTO createEmpleado(EmpleadoRequestDTO empleadoRequestDTO);
	EmpleadoResponseDTO actualizarEmpleado(Integer idEmpleado,EmpleadoRequestDTO empleadoRequestDTO);
	boolean borrarEmpleado(Integer idEmpleado);


}
