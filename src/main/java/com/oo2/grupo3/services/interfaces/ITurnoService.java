


package com.oo2.grupo3.services.interfaces;



import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import com.oo2.grupo3.models.dtos.requests.TurnoRequestDTO;
import com.oo2.grupo3.models.dtos.responses.TurnoResponseDTO;
import com.oo2.grupo3.models.entities.Turno;


public interface ITurnoService {
    List<Turno> findAll();
    Turno findById(Integer id);
    Turno save(Turno turno);

    void deleteById(Integer id);
    List<TurnoResponseDTO> obtenerTodosLosTurnos();
	Turno save(TurnoRequestDTO requestDTO);
	TurnoResponseDTO solicitarTurno(TurnoRequestDTO turnoRequestDTO);
	List<Turno> getAll();
	Page<TurnoResponseDTO> findAll(Pageable pageable);
	//Turno generarTurno(Integer idCliente, Integer idEmpleado, Integer idServicio, Integer idDia, Integer idHora);
	Turno generarTurno(Integer idCliente, Integer idEmpleado, Integer idServicio, LocalDate fecha, LocalTime hora);

	
}

