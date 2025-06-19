package com.oo2.grupo3.models.dtos.requests;

import java.time.LocalDate;
import java.time.LocalTime;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TurnoRequestDTO {

    @NotNull(message = "El ID del cliente es obligatorio")
    private Integer idCliente;

    @NotNull(message = "El ID del empleado es obligatorio")
    private Integer idEmpleado;

    @NotNull(message = "El ID del servicio es obligatorio")
    private Integer idServicio;
    
    @NotNull(message = "La fecha es obligatoria")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fecha;

    @NotNull(message = "La hora es obligatoria")
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime hora;

	
}




