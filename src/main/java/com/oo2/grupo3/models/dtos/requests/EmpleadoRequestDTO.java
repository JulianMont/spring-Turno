package com.oo2.grupo3.models.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

public class EmpleadoRequestDTO {
	
    @NotBlank(message = "El legajo es obligatorio")
    private String legajo;

    @NotNull(message = "La especialidad es obligatoria")
    private int especialidadId;

}
