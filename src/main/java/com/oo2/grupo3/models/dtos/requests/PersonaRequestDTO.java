package com.oo2.grupo3.models.dtos.requests;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


// ----import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor


// ----- @SuperBuilder

public class PersonaRequestDTO {
	
	private Integer idPersona;

	
    @NotBlank(message = "La Persona debe tener un nombre.")
    @Size(max = 20)
    private String nombre;
    
	@NotBlank(message = "La Persona debe tener un apellido.")
	@Size(max = 20)
    private String apellido;

    @NotNull(message = "El DNI es obligatorio.")
    @Min(value = 1000000, message = "El DNI debe ser mayor o igual a 1000000.")
    private Integer dni;
    
    @NotNull(message = "El usuario es obligatorio")
    private UserRequestDTO user;
    
    
}
