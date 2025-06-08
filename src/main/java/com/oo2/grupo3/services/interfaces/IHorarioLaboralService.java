package com.oo2.grupo3.services.interfaces;

import java.util.List;

import com.oo2.grupo3.models.dtos.requests.HorarioLaboralRequestDTO;
import com.oo2.grupo3.models.dtos.responses.HorarioLaboralResponseDTO;

public interface IHorarioLaboralService {
	
	List<HorarioLaboralResponseDTO> traerHorariosLaborales(Integer idEmpleado);
	HorarioLaboralResponseDTO findbyId(Integer id);
    HorarioLaboralResponseDTO agregarHorario(Integer idEmpleado,HorarioLaboralRequestDTO dto);
    HorarioLaboralResponseDTO editarHorario(Integer idEmpleado, Integer idHorarioLaboral, HorarioLaboralRequestDTO dto);
    boolean eliminarHorario(Integer idEmpleado, Integer idHorarioLaboral);

}
