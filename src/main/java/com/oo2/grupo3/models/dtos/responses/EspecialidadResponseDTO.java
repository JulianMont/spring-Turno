package com.oo2.grupo3.models.dtos.responses;

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

public class EspecialidadResponseDTO {
	
    private Long idEspecialidad;
  
    private String nombre;

}
