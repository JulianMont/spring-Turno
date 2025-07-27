package com.oo2.grupo3.models.dtos.requests;


import io.swagger.v3.oas.annotations.media.Schema;
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

public class EmpleadoRequestDTO extends PersonaRequestDTO {

	
    @NotBlank(message = "El legajo es obligatorio")
    @Schema(description = "Número de legajo del empleado", example = "EMP152AB")
    private String legajo;

    @NotNull(message = "La especialidad es obligatoria")
    @Schema(description = "ID de la especialidad", example = "2")
    private Long especialidadId;
   
}
