package com.oo2.grupo3.models.dtos.requests;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

public class AusenciaEmpleadoRequestDTO {
	

    @NotNull(message = "La fecha de ausencia es obligatoria")
    private LocalDate fecha;

    @NotBlank(message = "El motivo no puede estar vacío")
    private String motivo;

    
    @NotNull(message = "El ID del empleado es obligatorio")
    private Long empleadoId;

}
