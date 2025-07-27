package com.oo2.grupo3.models.dtos.record;

import java.util.List;

import com.oo2.grupo3.models.dtos.responses.AusenciaEmpleadoResponseDTO;
import com.oo2.grupo3.models.dtos.responses.EspecialidadResponseDTO;
import com.oo2.grupo3.models.dtos.responses.HorarioLaboralResponseDTO;
import com.oo2.grupo3.models.dtos.responses.PersonaResponseDTO;

public record EmpleadoRecordDTO (
		
		PersonaResponseDTO persona,
		String legajo,
	    EspecialidadResponseDTO especialidad,
		List<HorarioLaboralResponseDTO> horariosLaborales,
		List<AusenciaEmpleadoResponseDTO> diasAusentes


) {}
