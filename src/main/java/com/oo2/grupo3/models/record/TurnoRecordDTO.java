package com.oo2.grupo3.models.dtos.record;

import jakarta.validation.constraints.NotBlank;

public record TurnoRecordDTO(
		
		@NotBlank
		String  dia,
		
		@NotBlank
		String  hora
) 

{}