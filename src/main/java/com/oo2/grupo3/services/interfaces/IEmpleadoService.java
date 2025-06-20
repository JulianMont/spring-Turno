package com.oo2.grupo3.services.interfaces;


import java.time.DayOfWeek;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.oo2.grupo3.models.dtos.requests.EmpleadoRequestDTO;
import com.oo2.grupo3.models.dtos.responses.EmpleadoResponseDTO;
import com.oo2.grupo3.models.entities.Empleado;





public interface IEmpleadoService {

	//Lo usa cualquier usuario
	
	Page<EmpleadoResponseDTO> buscarEmpleadosFiltrados(String nombre, String legajo, Long especialidadId, Pageable pageable);

	Page<EmpleadoResponseDTO> findAll(Pageable pageable);
	
	Page<EmpleadoResponseDTO> findByIdEspecialidad(Long idEspecialidad , Pageable pageable);

	Page<EmpleadoResponseDTO> findByNombre(String nombre , Pageable pageable);
	
	EmpleadoResponseDTO findById(Integer id);
	

	
	//admin
	//lo usa la parte de gestion de empleado

  
	EmpleadoResponseDTO findByLegajo(String legajo);
	
	EmpleadoResponseDTO createEmpleado(EmpleadoRequestDTO empleadoRequestDTO);
	EmpleadoResponseDTO actualizarEmpleado(Integer idEmpleado,EmpleadoRequestDTO empleadoRequestDTO) throws Exception;
	boolean borrarEmpleado(Integer idEmpleado) throws Exception;

	
	public List<Empleado> getAllEmpleados();

	Object getAll();


}
