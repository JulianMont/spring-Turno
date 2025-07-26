package com.oo2.grupo3.models.dtos.record;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestRecordDTO( 
	
	@NotBlank
	String email,
	
	@NotBlank
	String password
)

{}