package com.oo2.grupo3.models.dtos.responses;

import java.util.List;

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


public class EmpleadoResponseDTO extends PersonaResponseDTO {

  
	
	//faltan datos de persona

    private String legajo;

    private EspecialidadResponseDTO especialidad;


    private List<HorarioLaboralResponseDTO> horariosLaborales;


    private List<AusenciaEmpleadoResponseDTO> diasAusentes;
	

}
