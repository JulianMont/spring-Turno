package com.oo2.grupo3.models.dtos.responses;

import java.util.List;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder

public class EmpleadoResponseDTO {
	
	//faltan datos de persona

    private String legajo;

    private EspecialidadResponseDTO especialidad;

    private Set<HorarioLaboralResponseDTO> horariosLaborales;

    private List<AusenciaEmpleadoResponseDTO> diasAusentes;
	

}
