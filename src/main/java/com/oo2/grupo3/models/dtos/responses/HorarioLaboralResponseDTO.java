package com.oo2.grupo3.models.dtos.responses;

import java.time.LocalTime;

import com.oo2.grupo3.models.enums.DiaSemana;

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


public class HorarioLaboralResponseDTO {
	
    private Long idHorarioLaboral;
    private DiaSemana diaSemana;
    private LocalTime horaInicio;
    private LocalTime horaFin;

}
