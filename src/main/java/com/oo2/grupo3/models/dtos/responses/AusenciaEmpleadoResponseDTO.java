package com.oo2.grupo3.models.dtos.responses;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder


public class AusenciaEmpleadoResponseDTO {
	

	private Integer idAusenciaEmpleado;
    private LocalDate fecha;
    private String motivo;
    
}
