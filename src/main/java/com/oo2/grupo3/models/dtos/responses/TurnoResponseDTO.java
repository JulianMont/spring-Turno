package com.oo2.grupo3.models.dtos.responses;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TurnoResponseDTO {

    private Integer idTurno;
    
    private Integer idCliente;      // agregado
    private Integer idEmpleado;     // agregado
    private Integer idServicio;     // agregado
    

    private String clienteNombre;

    private String empleadoNombre;

    private String servicioNombre;

    private LocalDate dia;

    private LocalTime hora;

    private String estado;
    

}
