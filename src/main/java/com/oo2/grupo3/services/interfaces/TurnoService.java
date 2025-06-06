package com.oo2.grupo3.services.interfaces;

import com.oo2.grupo3.models.dtos.requests.TurnoRequestDTO;
import com.oo2.grupo3.models.dtos.responses.TurnoResponseDTO;
import java.util.List;

public interface TurnoService {
	
    TurnoResponseDTO solicitarTurno(TurnoRequestDTO turnoRequestDTO);
    List<TurnoResponseDTO> obtenerTodosLosTurnos();
    
}
