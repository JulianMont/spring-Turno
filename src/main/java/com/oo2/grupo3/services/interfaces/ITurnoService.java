


package com.oo2.grupo3.services.interfaces;



import java.util.List;

import com.oo2.grupo3.models.dtos.requests.TurnoRequestDTO;
import com.oo2.grupo3.models.dtos.responses.TurnoResponseDTO;
import com.oo2.grupo3.models.entities.Turno;


public interface ITurnoService {
    List<Turno> findAll();
    Turno findById(Integer id);
    Turno save(Turno turno);

    void deleteById(Integer id);
    Turno generarTurno(Integer idCliente, Integer idEmpleado, Integer idServicio, Integer idDia, Integer idHora);
    List<TurnoResponseDTO> obtenerTodosLosTurnos();
	Turno save(TurnoRequestDTO requestDTO);
	TurnoResponseDTO solicitarTurno(TurnoRequestDTO turnoRequestDTO);
	
}

