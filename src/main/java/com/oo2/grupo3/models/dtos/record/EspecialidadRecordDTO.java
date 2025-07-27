package com.oo2.grupo3.models.dtos.record;

import io.swagger.v3.oas.annotations.media.Schema;

public record EspecialidadRecordDTO(

        @Schema(description = "Id de la especialidad", example = "1")
        Long idEspecialidad,

        @Schema(description = "Nombre de la especialidad", example = "Cardiología")
        String nombre
) {}
