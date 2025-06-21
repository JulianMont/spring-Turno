package com.oo2.grupo3.models.dtos.responses;

import java.util.List;
import java.util.ArrayList;
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

  
    private String legajo;

    private EspecialidadResponseDTO especialidad;



    @Builder.Default
    private List<HorarioLaboralResponseDTO> horariosLaborales = new ArrayList<>();

    @Builder.Default
    private List<AusenciaEmpleadoResponseDTO> diasAusentes = new ArrayList<>();
	

}
