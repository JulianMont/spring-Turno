
package com.oo2.grupo3.services.interfaces;

import java.util.List;

import com.oo2.grupo3.models.dtos.requests.EspecialidadRequestDTO;
import com.oo2.grupo3.models.dtos.responses.EspecialidadResponseDTO;

public interface IEspecialidadService {
	
	List<EspecialidadResponseDTO> traerEspecialidades();
	EspecialidadResponseDTO findById(Long id);
	EspecialidadResponseDTO crearEspecialidad(EspecialidadRequestDTO dtoEspecialidad);
	EspecialidadResponseDTO editarEspecialidad(Long id, EspecialidadRequestDTO dtoEspecialidad);
	boolean borrarEspecialidad(Long id);

}

