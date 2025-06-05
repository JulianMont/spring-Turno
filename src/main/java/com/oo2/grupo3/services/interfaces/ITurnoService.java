


package com.oo2.grupo3.services.interfaces;

import com.oo2.grupo3.models.dtos.requests.TurnoRequestDTO;
import com.oo2.grupo3.models.entities.Turno;

import java.util.List;

public interface ITurnoService {
    List<Turno> findAll();
    Turno findById(Integer id);
    Turno save(Turno turno);
    void deleteById(Integer id);
    Turno generarTurno(Integer idCliente, Integer idEmpleado, Integer idServicio, Integer idDia, Integer idHora);
	Turno save(TurnoRequestDTO requestDTO);
}

