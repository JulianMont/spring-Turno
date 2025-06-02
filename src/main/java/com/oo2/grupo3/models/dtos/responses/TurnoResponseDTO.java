package com.oo2.grupo3.models.dtos.responses;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Builder
public class TurnoResponseDTO {

    private Integer idTurno;

    private String clienteNombreCompleto;    
    private String empleadoNombreCompleto;   
    private String servicioNombre;
    private LocalDate fechaDia;
    private LocalTime hora;
    
    
    
    
}