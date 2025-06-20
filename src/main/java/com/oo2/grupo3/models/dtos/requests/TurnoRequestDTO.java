package com.oo2.grupo3.models.dtos.requests;

import java.time.LocalDate;
import java.time.LocalTime;
import org.springframework.format.annotation.DateTimeFormat;

import com.oo2.grupo3.models.enums.EstadoTurno;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TurnoRequestDTO {

	@NotNull(message = "Debe seleccionar un cliente")
	private Integer idCliente;

	@NotNull(message = "Debe seleccionar un empleado")
	private Integer idEmpleado;

	@NotNull(message = "Debe seleccionar un servicio")
	private Integer idServicio;
    
    @NotNull(message = "La fecha es obligatoria")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fecha;

    @NotNull(message = "La hora es obligatoria")
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime hora;

    private EstadoTurno estado;
	
}




