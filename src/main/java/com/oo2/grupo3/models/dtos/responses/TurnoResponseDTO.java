package com.oo2.grupo3.models.dtos.responses;

import java.time.LocalDate;
import java.time.LocalTime;

import com.oo2.grupo3.models.entities.Dia;
import com.oo2.grupo3.models.entities.Hora;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TurnoResponseDTO {

    private Integer idTurno;

    private String clienteNombre;

    private String empleadoNombre;

    private String servicioNombre;

    private LocalDate dia;

    private LocalTime hora;

    
    

}
