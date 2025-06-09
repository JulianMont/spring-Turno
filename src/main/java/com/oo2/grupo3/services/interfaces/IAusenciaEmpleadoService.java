package com.oo2.grupo3.services.interfaces;

import java.util.List;

import com.oo2.grupo3.models.dtos.requests.AusenciaEmpleadoRequestDTO;
import com.oo2.grupo3.models.dtos.responses.AusenciaEmpleadoResponseDTO;

public interface IAusenciaEmpleadoService {
	
	
	List<AusenciaEmpleadoResponseDTO> traerAusenciasEmpleado(Integer idEmpleado);
	
	AusenciaEmpleadoResponseDTO findbyId(Integer id);
    AusenciaEmpleadoResponseDTO agregarAusencia(Integer idEmpleado,AusenciaEmpleadoRequestDTO dto);
    AusenciaEmpleadoResponseDTO modificarAusencia(Integer idEmpleado,Integer idAusencia, AusenciaEmpleadoRequestDTO dto);
    boolean eliminarAusencia(Integer idEmpleado, Integer idAusencia);

}
